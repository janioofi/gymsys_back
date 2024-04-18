package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PagamentoDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.enums.FormaPagamento;
import br.janioofi.msgym.domain.services.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class PagamentoControllerTest {

    private static final Integer INDEX   = 0;
    private static final Long ID = 1L;
    private static final FormaPagamento FORMA_PAGAMENTO = FormaPagamento.PIX;
    private static final BigDecimal VALOR = BigDecimal.valueOf(90);
    private static final LocalDateTime DATA_PAGAMENTO = LocalDateTime.now();
    private static final Plano PLANO = new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20));
    private static final Cliente CLIENTE = new Cliente(1L, "Janio", "Filho", "Nen", "52315278821", "janio@gmail.com",  LocalDate.of(2001, Month.JUNE, 1), PLANO, LocalDate.now(), LocalDateTime.now());

    @InjectMocks
    private PagamentoController controller;

    @Mock
    private PagamentoService service;

    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPagamento();
        controller = new PagamentoController(service);
    }

    @Test
    void whenFindAllThenReturnListOfPagamento() {
        when(service.findAll()).thenReturn(List.of(pagamento));

        ResponseEntity<List<Pagamento>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Pagamento.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).getId_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getBody().get(INDEX).getData_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getBody().get(INDEX).getForma_pagamento());
        assertEquals(VALOR, response.getBody().get(INDEX).getValor());
        assertEquals(PLANO, response.getBody().get(INDEX).getPlano());
        assertEquals(CLIENTE, response.getBody().get(INDEX).getCliente());

    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(pagamento);

        ResponseEntity<Pagamento> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Pagamento.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getBody().getData_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getBody().getForma_pagamento());
        assertEquals(VALOR, response.getBody().getValor());
        assertEquals(PLANO, response.getBody().getPlano());
        assertEquals(CLIENTE, response.getBody().getCliente());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(pagamento);

        ResponseEntity<Pagamento> response = controller.create(pagamentoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(pagamento);

        ResponseEntity<Pagamento> response = controller.update(ID, pagamentoDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Pagamento.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getBody().getData_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getBody().getForma_pagamento());
        assertEquals(VALOR, response.getBody().getValor());
        assertEquals(PLANO, response.getBody().getPlano());
        assertEquals(CLIENTE, response.getBody().getCliente());
        assertEquals(PLANO, response.getBody().getPlano());
    }

    private void startPagamento() {
        pagamento = new Pagamento(ID, DATA_PAGAMENTO, FORMA_PAGAMENTO,  CLIENTE, PLANO, VALOR);
        pagamentoDTO = new PagamentoDTO(ID, FORMA_PAGAMENTO, 1L, 1L, VALOR);
    }
}