package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.PagamentoDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.enums.FormaPagamento;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.InvalidException;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class PagamentoServiceTest {

    private static final Integer INDEX   = 0;
    private static final Long ID = 1L;
    private static final FormaPagamento FORMA_PAGAMENTO = FormaPagamento.PIX;
    private static final BigDecimal VALOR = BigDecimal.valueOf(90);
    private static final LocalDateTime DATA_PAGAMENTO = LocalDateTime.now();
    private static final LocalDateTime DATA_ATUALIZACAO = LocalDateTime.now();
    private static final String OBSERVACAO = "Teste";
    private static final Plano PLANO = new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(90), 1L);
    private static final Cliente CLIENTE = new Cliente(1L, "Janio", "Filho", "Nen", "52315278821", "janio@gmail.com",  LocalDate.of(2001, Month.JUNE, 1), PLANO, LocalDate.now(), LocalDateTime.now());

    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;
    private PagamentoDTO pagamentoDTOError;
    private Optional<Pagamento> optionalPagamento;
    private Optional<Plano> optionalPlano;
    private Optional<Cliente> optionalCliente;

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PlanoRepository planoRepository;
    @Mock
    private EmailProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPagamento();
        service = new PagamentoService(repository, clienteRepository, planoRepository, producer);
    }

    @Test
    void whenFindByIdThenReturnAPagamentoInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalPagamento);
        PagamentoDTO response = service.findById(ID);
        assertNotNull(response);
        assertEquals(PagamentoDTO.class, response.getClass());
        assertEquals(ID, response.id_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.forma_pagamento());
        assertEquals(DATA_PAGAMENTO, response.data_pagamento());
        assertEquals(VALOR, response.valor());
        assertEquals(CLIENTE.getId_cliente(), response.id_cliente());
        assertEquals(CLIENTE.getNome(), response.cliente());
        assertEquals(PLANO.getDescricao(), response.plano());
    }

    @Test
    void whenFindByIdThenReturnAnRecordNotFoundException(){
        when(repository.findById(anyLong())).thenThrow(new RecordNotFoundException("Objeto não encontrado"));

        try{
            service.findById(ID);
        }catch (Exception e){
            assertEquals(RecordNotFoundException.class, e.getClass());
            assertEquals("Objeto não encontrado", e.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAListOfPagamentos() {
        when(repository.findAll()).thenReturn(List.of(pagamento));
        List<PagamentoDTO> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(PagamentoDTO.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).id_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.get(INDEX).forma_pagamento());
        assertEquals(DATA_PAGAMENTO, response.get(INDEX).data_pagamento());
        assertEquals(VALOR, response.get(INDEX).valor());
        assertEquals(CLIENTE.getId_cliente(), response.get(INDEX).id_cliente());
        assertEquals(CLIENTE.getNome(), response.get(INDEX).cliente());
        assertEquals(PLANO.getDescricao(), response.get(INDEX).plano());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(planoRepository.findById(anyLong())).thenReturn(optionalPlano);
        when(clienteRepository.findById(anyLong())).thenReturn(optionalCliente);
        when(repository.save(any())).thenReturn(pagamento);

        PagamentoDTO response = service.create(pagamentoDTO);

        assertNotNull(response);
        assertEquals(PagamentoDTO.class, response.getClass());
        assertEquals(ID, response.id_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.forma_pagamento());
        assertEquals(DATA_PAGAMENTO, response.data_pagamento());
        assertEquals(VALOR, response.valor());
        assertEquals(CLIENTE.getId_cliente(), response.id_cliente());
        assertEquals(CLIENTE.getNome(), response.cliente());
        assertEquals(PLANO.getDescricao(), response.plano());
    }

    @Test
    void whenCreateThenInvalidException() {
        when(planoRepository.findById(anyLong())).thenReturn(optionalPlano);
        when(clienteRepository.findById(anyLong())).thenReturn(optionalCliente);
        when(repository.save(any())).thenReturn(pagamento);
        try{
            PagamentoDTO response = service.create(pagamentoDTOError);
        }catch (Exception e){
            assertEquals(InvalidException.class, e.getClass());
        }
    }

    @Test
    void whenUpdateThenReturnAnRecordNotFoundException() {
        when(repository.save(any())).thenReturn(pagamento);

        try {
            service.update(ID, pagamentoDTO);
        }catch (Exception e){
            assertEquals(RecordNotFoundException.class, e.getClass());
        }
    }

    private void startPagamento() {
        pagamento = new Pagamento(ID, DATA_PAGAMENTO, DATA_ATUALIZACAO, FORMA_PAGAMENTO, CLIENTE, PLANO, VALOR, OBSERVACAO);
        pagamentoDTO = new PagamentoDTO(ID, DATA_PAGAMENTO, DATA_ATUALIZACAO, FORMA_PAGAMENTO, PLANO.getDescricao(), CLIENTE.getNome(), 1L, VALOR, OBSERVACAO);
        pagamentoDTOError = new PagamentoDTO(ID, DATA_PAGAMENTO, DATA_ATUALIZACAO, FORMA_PAGAMENTO, PLANO.getDescricao(), CLIENTE.getNome(), 1L, BigDecimal.valueOf(30), OBSERVACAO);
        optionalPagamento = Optional.of(new Pagamento(ID, DATA_PAGAMENTO, DATA_ATUALIZACAO, FORMA_PAGAMENTO, CLIENTE, PLANO, VALOR, OBSERVACAO));
        optionalCliente = Optional.of(CLIENTE);
        optionalPlano = Optional.of(PLANO);
    }
}