package br.janioofi.msgym.domain.services;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.PagamentoDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.InvalidException;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final EmailProducer producer;

    private static final LocalDateTime NOW = LocalDateTime.now();

    public List<Pagamento> findAll(){
        log.info("Listando todos os pagamentos");
        return repository.findAll();
    }

    public Pagamento findById(Long id){
        log.info("Buscando pagamento por ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum pagamento encontrado com ID: " + id));
    }

    public Pagamento create(PagamentoDTO pagamentoDTO){
        Pagamento pagamento = new Pagamento();
        Cliente cliente = clienteRepository.findById(pagamentoDTO.cliente()).orElseThrow(() ->  new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + pagamentoDTO.cliente()));
        Plano plano = planoRepository.findById(cliente.getPlano().getId_plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + cliente.getPlano().getId_plano()));

        pagamento.setData_pagamento(NOW);
        pagamento.setForma_pagamento(pagamentoDTO.forma_pagamento());
        pagamento.setValor(pagamentoDTO.valor());
        pagamento.setPlano(plano);
        pagamento.setCliente(cliente);

        log.info("Novo pagamento gerado: " + pagamento);
        sendEmailPagamentoRealizado(cliente, plano, pagamento);
        verificaPagamento(pagamento, plano);
        return repository.save(pagamento);
    }

    public Pagamento update(Long id, PagamentoDTO pagamentoDTO){
        Cliente cliente = clienteRepository.findById(pagamentoDTO.cliente()).orElseThrow(() ->  new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + pagamentoDTO.cliente()));
        Plano plano = planoRepository.findById(cliente.getPlano().getId_plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + cliente.getPlano().getId_plano()));
        return repository.findById(id).map(pagamento -> {
            pagamento.setCliente(cliente);
            pagamento.setPlano(plano);
            pagamento.setValor(pagamentoDTO.valor());
            verificaPagamento(pagamento, plano);
            return repository.save(pagamento);
        }).orElseThrow(() -> new RecordNotFoundException("Nenhum pagamento encontrado com ID: " + id));
    }

    public Pagamento findByUltimoPagamento(Long id_cliente){
        return repository.findByUltimoPagamento(id_cliente).orElseThrow();
    }

    private void sendEmailPagamentoRealizado(Cliente cliente, Plano plano, Pagamento pagamento){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = pagamento.getData_pagamento().format(formatter);
        EmailDto email = new EmailDto(cliente.getEmail(), "Pagamento Realizado", "Foi realizado o pagamento do seu plano na Gym Sys, confira os detalhes:\n" +
                "Data de pagamento: " + data +"\n" +
                "Plano que foi pago: " + plano.getDescricao() +"\n" +
                "Valor pago: " + pagamento.getValor() + "\n" +
                "Forma de pagamento: " + pagamento.getForma_pagamento() + "\n");
        producer.publishMessageEmail(email);
    }

    private void verificaPagamento(Pagamento pagamento, Plano plano){
        if(pagamento.getValor().doubleValue() != plano.getPreco().doubleValue()){
            throw new InvalidException("O valor que está sendo pago é diferente de " + plano.getPreco() + ", do plano " + plano.getDescricao());
        }
    }

}
