package br.janioofi.msgym.domain.services;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.InvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
@Slf4j
public class ValidacaoCliente {

    private final PagamentoRepository pagamentoRepository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final EmailProducer producer;

    private static final LocalDateTime NOW = LocalDateTime.now();

    public void situacaoCliente(Long id_cliente){
        Pagamento pagamento = pagamentoRepository.findByUltimoPagamento(id_cliente);
        Plano plano = planoRepository.findById(pagamento.getPlano().getId_plano()).orElseThrow();
        Cliente cliente = clienteRepository.findById(id_cliente).orElseThrow();

        LocalDateTime dataValidaPagamento = pagamento.getData_pagamento().plusMonths(plano.getQuantidadeMeses());
        LocalDateTime dataBloqueio = dataValidaPagamento.plusDays(5);

        if(NOW.isAfter(dataValidaPagamento) && NOW.isBefore(dataBloqueio)){
            sendEmailPagamentoPendente(cliente, dataBloqueio);
        }
        acessoComAtencao(cliente, dataValidaPagamento, dataBloqueio);
        acessoBloqueado(cliente, dataBloqueio);
    }

    private void acessoComAtencao(Cliente cliente, LocalDateTime dataValidaPagamento, LocalDateTime dataBloqueio){
        if(NOW.isAfter(dataValidaPagamento) && NOW.isBefore(dataBloqueio)){
            sendEmailPagamentoPendente(cliente, dataBloqueio);
        }
    }

    private void acessoBloqueado(Cliente cliente, LocalDateTime dataBloqueio){
        if(dataBloqueio.isBefore(NOW)){
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
