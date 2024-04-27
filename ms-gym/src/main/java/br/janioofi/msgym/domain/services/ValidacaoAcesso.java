package br.janioofi.msgym.domain.services;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.entities.Acesso;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.enums.TipoRegistro;
import br.janioofi.msgym.domain.repositories.AcessoRepository;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.InvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ValidacaoAcesso {

    private final PagamentoRepository pagamentoRepository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final EmailProducer producer;
    private final AcessoRepository acessoRepository;

    public void verificaEntrada(Long id_cliente){
        Optional<Pagamento> pagamento = pagamentoRepository.findByUltimoPagamento(id_cliente);
        if(pagamento.isEmpty()) throw new InvalidException("Nenhum pagamento efetuado");
        Cliente cliente = clienteRepository.findById(id_cliente).orElseThrow();
        Plano plano = planoRepository.findById(cliente.getPlano().getId_plano()).orElseThrow();

        LocalDateTime dataValidaPagamento = pagamento.get().getData_pagamento().plusMonths(plano.getQuantidadeMeses());
        LocalDateTime dataBloqueio = dataValidaPagamento.plusDays(5);

        if(LocalDateTime.now().isAfter(dataValidaPagamento) && LocalDateTime.now().isBefore(dataBloqueio)){
            sendEmailPagamentoPendente(cliente, dataBloqueio);
        }
        acessoComAtencao(cliente, dataValidaPagamento, dataBloqueio);
        acessoBloqueado(cliente, dataBloqueio);
        verificaUltimaEntrada(id_cliente);
    }

    public void  verificaSaida(Long id_cliente){
        verificaUltimoRegistro(id_cliente);
    }

    private void verificaUltimoRegistro(Long id_cliente){
        Optional<Acesso> acesso = acessoRepository.ultimoRegistro(id_cliente);
        if(acesso.isPresent() && (acesso.get().getTipoRegistro() == TipoRegistro.SAIDA)) {throw new InvalidException("Necessário realizar uma entrada");
        }
    }

    private void verificaUltimaEntrada(Long id_cliente){
        Optional<Acesso> acesso = acessoRepository.ultimaEntrada(id_cliente);
        if(acesso.isPresent()){
            LocalDateTime tempoMinimo = acesso.get().getData_registro().plusMinutes(20); // TEMPO MINIMO QUE A PESSOA PODE REALIZAR OUTRO ACESSO
            if(LocalDateTime.now().isBefore(tempoMinimo)) throw new InvalidException("Você realizou um acesso nos últimos 30 minutos");
        }
    }

    private void acessoComAtencao(Cliente cliente, LocalDateTime dataValidaPagamento, LocalDateTime dataBloqueio){
        if(LocalDateTime.now().isAfter(dataValidaPagamento) && LocalDateTime.now().isBefore(dataBloqueio)) sendEmailPagamentoPendente(cliente, dataBloqueio);
    }

    private void acessoBloqueado(Cliente cliente, LocalDateTime dataBloqueio){
        if(dataBloqueio.isBefore(LocalDateTime.now())){
            sendEmailAcessoBloqueado(cliente);
            throw new InvalidException("Pagamento do plano está em atraso. Já se passaram mais de 5 dias da data limite para pagamento, por isso seu acesso está bloqueado" +
                    ". Para ter acesso novamente, acerte suas pendências na recepção.");
        }
    }

    private void sendEmailPagamentoPendente(Cliente cliente, LocalDateTime dataLimite){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = dataLimite.format(formatter);
        EmailDto email = new EmailDto(cliente.getEmail(), "Pagamento", "Acerte o seu pagamento antes de " + data + " para evitar qualquer bloqueio do seu cadastro");
        producer.publishMessageEmail(email);
    }

    private void sendEmailAcessoBloqueado(Cliente cliente){
        EmailDto email = new EmailDto(cliente.getEmail(), "Acesso Bloqueado", "Pagamento do plano está em atraso. Já se passaram mais de 5 dias da data limite para pagamento, por isso seu acesso está bloqueado" +
                ". Para ter acesso novamente, acerte suas pendências na recepção.");
        producer.publishMessageEmail(email);
    }
}
