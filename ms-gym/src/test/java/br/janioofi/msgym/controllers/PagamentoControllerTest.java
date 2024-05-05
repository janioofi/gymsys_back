package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PagamentoDTO;
import br.janioofi.msgym.domain.entities.Cliente;
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
    private static final LocalDateTime DATA_ATUALIZACAO = LocalDateTime.now();
    private static final Plano PLANO = new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20), 1L);
    private static final Cliente CLIENTE = new Cliente(1L, "Janio", "Filho", "Nen", "52315278821", "janio@gmail.com",  LocalDate.of(2001, Month.JUNE, 1), PLANO, LocalDate.now(), LocalDateTime.now());

    @InjectMocks
    private PagamentoController controller;

    @Mock
    private PagamentoService service;

    private PagamentoDTO pagamentoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPagamento();
        controller = new PagamentoController(service);
    }

    @Test
    void whenFindAllThenReturnListOfPagamento() {
        when(service.findAll()).thenReturn(List.of(pagamentoDTO));

        ResponseEntity<List<PagamentoDTO>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PagamentoDTO.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).id_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getBody().get(INDEX).data_pagamento());
        assertEquals(DATA_ATUALIZACAO, response.getBody().get(INDEX).data_atualizacao());
        assertEquals(FORMA_PAGAMENTO, response.getBody().get(INDEX).forma_pagamento());
        assertEquals(VALOR, response.getBody().get(INDEX).valor());
        assertEquals(PLANO.getDescricao(), response.getBody().get(INDEX).plano());
        assertEquals(CLIENTE.getId_cliente(), response.getBody().get(INDEX).id_cliente());
        assertEquals(CLIENTE.getNome(), response.getBody().get(INDEX).cliente());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(pagamentoDTO);

        ResponseEntity<PagamentoDTO> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PagamentoDTO.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().id_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getBody().data_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getBody().forma_pagamento());
        assertEquals(VALOR, response.getBody().valor());
        assertEquals(PLANO.getDescricao(), response.getBody().plano());
        assertEquals(CLIENTE.getId_cliente(), response.getBody().id_cliente());
        assertEquals(CLIENTE.getNome(), response.getBody().cliente());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(pagamentoDTO);

        ResponseEntity<PagamentoDTO> response = controller.create(pagamentoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(pagamentoDTO);

        ResponseEntity<PagamentoDTO> response = controller.update(ID, pagamentoDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PagamentoDTO.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().id_pagamento());
        assertEquals(DATA_PAGAMENTO, response.getBody().data_pagamento());
        assertEquals(FORMA_PAGAMENTO, response.getBody().forma_pagamento());
        assertEquals(VALOR, response.getBody().valor());
        assertEquals(PLANO.getDescricao(), response.getBody().plano());
        assertEquals(CLIENTE.getId_cliente(), response.getBody().id_cliente());
        assertEquals(CLIENTE.getNome(), response.getBody().cliente());
    }

    private void startPagamento() {
        pagamentoDTO = new PagamentoDTO(ID, DATA_PAGAMENTO, DATA_ATUALIZACAO, FORMA_PAGAMENTO, PLANO.getDescricao(), CLIENTE.getNome(), 1L, VALOR, "");
    }
}