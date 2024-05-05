package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.services.ClienteService;
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
class ClienteControllerTest {

    private static final Integer INDEX   = 0;
    private static final Long ID = 1L;
    private static final String NOME   = "admin";
    private static final String SOBRENOME = "admin";
    private static final String APELIDO = "admin";
    private static final String CPF = "72288363088";
    private static final String EMAIL = "janio@gmail.com";
    private static final LocalDate DATA_NASCIMENTO = LocalDate.of(2001, 9, 7);
    private static final Plano PLANO = new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20), 1L);

    private ClienteDTO clienteDTO;

    @InjectMocks
    private ClienteController controller;

    @Mock
    private ClienteService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCliente();
        controller = new ClienteController(service);
    }

    @Test
    void whenFindAllThenReturnListOfCliente() {
        when(service.findAll()).thenReturn(List.of(clienteDTO));
        ResponseEntity<List<ClienteDTO>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ClienteDTO.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).id_cliente());
        assertEquals(NOME, response.getBody().get(INDEX).nome());
        assertEquals(SOBRENOME, response.getBody().get(INDEX).sobrenome());
        assertEquals(APELIDO, response.getBody().get(INDEX).apelido());
        assertEquals(CPF, response.getBody().get(INDEX).cpf());
        assertEquals(EMAIL, response.getBody().get(INDEX).email());
        assertEquals(DATA_NASCIMENTO, response.getBody().get(INDEX).data_nascimento());
        assertEquals(PLANO.getDescricao(), response.getBody().get(INDEX).plano());
        assertEquals(PLANO.getId_plano(), response.getBody().get(INDEX).id_plano());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ClienteDTO.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().id_cliente());
        assertEquals(NOME, response.getBody().nome());
        assertEquals(SOBRENOME, response.getBody().sobrenome());
        assertEquals(APELIDO, response.getBody().apelido());
        assertEquals(CPF, response.getBody().cpf());
        assertEquals(EMAIL, response.getBody().email());
        assertEquals(DATA_NASCIMENTO, response.getBody().data_nascimento());
        assertEquals(PLANO.getDescricao(), response.getBody().plano());
        assertEquals(PLANO.getId_plano(), response.getBody().id_plano());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(clienteDTO);
        ResponseEntity<ClienteDTO> response = controller.create(clienteDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = controller.update(ID, clienteDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ClienteDTO.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().id_cliente());
        assertEquals(NOME, response.getBody().nome());
        assertEquals(SOBRENOME, response.getBody().sobrenome());
        assertEquals(APELIDO, response.getBody().apelido());
        assertEquals(CPF, response.getBody().cpf());
        assertEquals(EMAIL, response.getBody().email());
        assertEquals(DATA_NASCIMENTO, response.getBody().data_nascimento());
        assertEquals(PLANO.getDescricao(), response.getBody().plano());
        assertEquals(PLANO.getId_plano(), response.getBody().id_plano());
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

    private void startCliente() {
        clienteDTO = new ClienteDTO(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, PLANO.getDescricao(), PLANO.getId_plano());
    }
}