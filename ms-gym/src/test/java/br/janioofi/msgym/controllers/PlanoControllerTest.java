package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PlanoDTO;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.services.PlanoService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlanoControllerTest {

    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String DESCRICAO   = "MENSAL";
    public static final LocalDate VIGENCIA = LocalDate.now();
    public static final BigDecimal PRECO = new BigDecimal("90.2");
    private static final Integer QTD = 1;

    private PlanoDTO planoDTO;
    private Plano plano;

    @InjectMocks
    private PlanoController controller;

    @Mock
    private PlanoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPlano();
        controller = new PlanoController(service);
    }

    @Test
    void whenFindAllThenReturnListOfPlanoDTO() {
        when(service.findAll()).thenReturn(List.of(plano));

        ResponseEntity<List<Plano>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Plano.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).getId_plano());
        assertEquals(DESCRICAO, response.getBody().get(INDEX).getDescricao());
        assertEquals(VIGENCIA, response.getBody().get(INDEX).getVigencia());
        assertEquals(PRECO, response.getBody().get(INDEX).getPreco());
        assertEquals(QTD, response.getBody().get(INDEX).getQuantidadeMeses());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(plano);

        ResponseEntity<Plano> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(ID, response.getBody().getId_plano());
        assertEquals(DESCRICAO, response.getBody().getDescricao());
        assertEquals(VIGENCIA, response.getBody().getVigencia());
        assertEquals(PRECO, response.getBody().getPreco());
        assertEquals(QTD, response.getBody().getQuantidadeMeses());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(plano);

        ResponseEntity<Plano> response = controller.create(planoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(plano);

        ResponseEntity<Plano> response = controller.update(ID, planoDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Plano.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(ID, response.getBody().getId_plano());
        assertEquals(DESCRICAO, response.getBody().getDescricao());
        assertEquals(VIGENCIA, response.getBody().getVigencia());
        assertEquals(PRECO, response.getBody().getPreco());
        assertEquals(QTD, response.getBody().getQuantidadeMeses());

    }

    @Test
    void whenDeleteThenReturnSuccess() {
        doNothing().when(service).delete(anyLong());
        ResponseEntity<Void> response = controller.delete(ID);
        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        verify(service, times(1)).delete(anyLong());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    private void startPlano() {
        planoDTO = new PlanoDTO(ID, DESCRICAO, VIGENCIA,  PRECO, QTD);
        plano = new Plano(ID, DESCRICAO, VIGENCIA, PRECO, QTD);
    }
}