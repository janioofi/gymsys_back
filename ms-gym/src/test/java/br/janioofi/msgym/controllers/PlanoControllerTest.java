package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PlanoDTO;
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
    private static final Long QTD = 1L;

    private PlanoDTO planoDTO;

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
        when(service.findAll()).thenReturn(List.of(planoDTO));

        ResponseEntity<List<PlanoDTO>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PlanoDTO.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).id_plano());
        assertEquals(DESCRICAO, response.getBody().get(INDEX).descricao());
        assertEquals(VIGENCIA, response.getBody().get(INDEX).vigencia());
        assertEquals(PRECO, response.getBody().get(INDEX).preco());
        assertEquals(QTD, response.getBody().get(INDEX).quantidadeMeses());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(planoDTO);

        ResponseEntity<PlanoDTO> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(ID, response.getBody().id_plano());
        assertEquals(DESCRICAO, response.getBody().descricao());
        assertEquals(VIGENCIA, response.getBody().vigencia());
        assertEquals(PRECO, response.getBody().preco());
        assertEquals(QTD, response.getBody().quantidadeMeses());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(planoDTO);

        ResponseEntity<PlanoDTO> response = controller.create(planoDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(planoDTO);

        ResponseEntity<PlanoDTO> response = controller.update(ID, planoDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(PlanoDTO.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(ID, response.getBody().id_plano());
        assertEquals(DESCRICAO, response.getBody().descricao());
        assertEquals(VIGENCIA, response.getBody().vigencia());
        assertEquals(PRECO, response.getBody().preco());
        assertEquals(QTD, response.getBody().quantidadeMeses());

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
    }
}