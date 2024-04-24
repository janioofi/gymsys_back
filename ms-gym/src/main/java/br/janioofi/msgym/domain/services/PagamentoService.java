package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.PagamentoDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;
    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;

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
        Plano plano = planoRepository.findById(pagamentoDTO.plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + pagamentoDTO.plano()));
        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setForma_pagamento(pagamentoDTO.forma_pagamento());
        pagamento.setValor(pagamentoDTO.valor());
        pagamento.setPlano(plano);
        pagamento.setCliente(cliente);
        log.info("Novo pagamento gerado: " + pagamento);
        return repository.save(pagamento);
    }

    public Pagamento update(Long id ,PagamentoDTO pagamentoDTO){
        Cliente cliente = clienteRepository.findById(pagamentoDTO.cliente()).orElseThrow(() ->  new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + pagamentoDTO.cliente()));
        Plano plano = planoRepository.findById(pagamentoDTO.plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + pagamentoDTO.plano()));
        return repository.findById(id).map(recordFound -> {
            recordFound.setCliente(cliente);
            recordFound.setPlano(plano);
            recordFound.setValor(pagamentoDTO.valor());
            return repository.save(recordFound);
        }).orElseThrow(() -> new RecordNotFoundException("Nenhum pagamento encontrado com ID: " + id));
    }

    public Pagamento findByUltimoPagamento(Long id_cliente){
        return repository.findByUltimoPagamento(id_cliente);
    }
}
