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
        Pagamento response = service.findById(ID);
        assertNotNull(response);
        assertEquals(Pagamento.class, response.getClass());
        assertEquals(ID, response.getId_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getForma_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getData_pagamento());
        assertEquals(VALOR, response.getValor());
        assertEquals(CLIENTE, response.getCliente());
        assertEquals(PLANO, response.getPlano());
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
        List<Pagamento> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Pagamento.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.get(INDEX).getForma_pagamento());
        assertEquals(DATA_PAGAMENTO, response.get(INDEX).getData_pagamento());
        assertEquals(VALOR, response.get(INDEX).getValor());
        assertEquals(CLIENTE, response.get(INDEX).getCliente());
        assertEquals(PLANO, response.get(INDEX).getPlano());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(planoRepository.findById(anyLong())).thenReturn(optionalPlano);
        when(clienteRepository.findById(anyLong())).thenReturn(optionalCliente);
        when(repository.save(any())).thenReturn(pagamento);

        Pagamento response = service.create(pagamentoDTO);

        assertNotNull(response);
        assertEquals(Pagamento.class, response.getClass());
        assertEquals(ID, response.getId_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getForma_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getData_pagamento());
        assertEquals(VALOR, response.getValor());
        assertEquals(CLIENTE, response.getCliente());
        assertEquals(PLANO, response.getPlano());
    }

    @Test
    void whenCreateThenInvalidException() {
        when(planoRepository.findById(anyLong())).thenReturn(optionalPlano);
        when(clienteRepository.findById(anyLong())).thenReturn(optionalCliente);
        when(repository.save(any())).thenReturn(pagamento);
        try{
            Pagamento response = service.create(pagamentoDTOError);
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
        pagamento = new Pagamento(ID, DATA_PAGAMENTO, FORMA_PAGAMENTO,  CLIENTE, PLANO, VALOR);
        pagamentoDTO = new PagamentoDTO(ID, FORMA_PAGAMENTO, 1L, VALOR);
        pagamentoDTOError = new PagamentoDTO(ID, FORMA_PAGAMENTO, 1L, BigDecimal.valueOf(30));
        optionalPagamento = Optional.of(new Pagamento(ID, DATA_PAGAMENTO, FORMA_PAGAMENTO,  CLIENTE, PLANO, VALOR));
        optionalCliente = Optional.of(CLIENTE);
        optionalPlano = Optional.of(PLANO);
    }
}