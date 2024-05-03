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

    public List<PagamentoDTO> findAll(){
        log.info("Listando todos os pagamentos");
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public PagamentoDTO findById(Long id){
        log.info("Buscando pagamento por ID: " + id);
        return this.mapToDTO(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum pagamento encontrado com ID: " + id)));
    }

    public PagamentoDTO create(PagamentoDTO pagamentoDTO){
        Pagamento pagamento = new Pagamento();
        Cliente cliente = clienteRepository.findById(pagamentoDTO.id_cliente()).orElseThrow(() ->  new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + pagamentoDTO.cliente()));
        Plano plano = planoRepository.findById(cliente.getPlano().getId_plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + cliente.getPlano().getId_plano()));

        pagamento.setData_pagamento(LocalDateTime.now());
        pagamento.setForma_pagamento(pagamentoDTO.forma_pagamento());
        pagamento.setValor(pagamentoDTO.valor());
        pagamento.setPlano(plano);
        pagamento.setCliente(cliente);
        pagamento.setObservacao(pagamentoDTO.observacao());

        log.info("Novo pagamento gerado: " + pagamento);
        sendEmailPagamentoRealizado(cliente, plano, pagamento);
        verificaPagamento(pagamento, plano);
        return this.mapToDTO(repository.save(pagamento));
    }

    public PagamentoDTO update(Long id, PagamentoDTO pagamentoDTO){
        Cliente cliente = clienteRepository.findById(pagamentoDTO.id_cliente()).orElseThrow(() ->  new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + pagamentoDTO.cliente()));
        Plano plano = planoRepository.findById(cliente.getPlano().getId_plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + cliente.getPlano().getId_plano()));
        Pagamento pag = repository.findById(id).map(pagamento -> {
            pagamento.setCliente(cliente);
            pagamento.setPlano(plano);
            pagamento.setValor(pagamentoDTO.valor());
            pagamento.setObservacao(pagamento.getObservacao());
            verificaPagamento(pagamento, plano);
            return repository.save(pagamento);
        }).orElseThrow(() -> new RecordNotFoundException("Nenhum pagamento encontrado com ID: " + id));
        return this.mapToDTO(pag);
    }

    public List<PagamentoDTO> pagamentosPoPeriodo(String data_inicio, String data_final){
        List<Pagamento> pagamentos = repository.pagamentosPorPeriodo(data_inicio, data_final);
        return pagamentos.stream()
                .map(this::mapToDTO).toList();
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

    private PagamentoDTO mapToDTO(Pagamento pagamento) {
        return new PagamentoDTO(pagamento.getId_pagamento(), pagamento.getData_pagamento(),pagamento.getForma_pagamento(), pagamento.getPlano().getDescricao(),pagamento.getCliente().getNome(), pagamento.getCliente().getId_cliente(), pagamento.getValor(), pagamento.getObservacao());
    }

}
