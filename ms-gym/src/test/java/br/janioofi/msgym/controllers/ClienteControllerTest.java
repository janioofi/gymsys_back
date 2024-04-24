package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Cliente;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class ClienteControllerTest {
    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String NOME   = "admin";
    public static final String SOBRENOME = "admin";
    public static final String APELIDO = "admin";
    public static final String CPF = "72288363088";
    public static final String EMAIL = "janio@gmail.com";
    public static final LocalDate DATA_NASCIMENTO = LocalDate.of(2001, 9, 7);
    public static final LocalDate DATA_CADASTRO = LocalDate.now();
    public static final LocalDateTime DATA_ATUALIZACAO = LocalDateTime.now();
    public static final Plano PLANO = new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20));

    private Cliente cliente = new Cliente();
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
        when(service.findAll()).thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Cliente.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).getId_cliente());
        assertEquals(NOME, response.getBody().get(INDEX).getNome());
        assertEquals(SOBRENOME, response.getBody().get(INDEX).getSobrenome());
        assertEquals(APELIDO, response.getBody().get(INDEX).getApelido());
        assertEquals(CPF, response.getBody().get(INDEX).getCpf());
        assertEquals(EMAIL, response.getBody().get(INDEX).getEmail());
        assertEquals(DATA_CADASTRO, response.getBody().get(INDEX).getData_cadastro());
        assertEquals(DATA_NASCIMENTO, response.getBody().get(INDEX).getData_nascimento());
        assertEquals(DATA_ATUALIZACAO, response.getBody().get(INDEX).getData_atualizacao());
        assertEquals(PLANO, response.getBody().get(INDEX).getPlano());

    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(cliente);

        ResponseEntity<Cliente> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Cliente.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId_cliente());
        assertEquals(NOME, response.getBody().getNome());
        assertEquals(SOBRENOME, response.getBody().getSobrenome());
        assertEquals(APELIDO, response.getBody().getApelido());
        assertEquals(CPF, response.getBody().getCpf());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(DATA_CADASTRO, response.getBody().getData_cadastro());
        assertEquals(DATA_NASCIMENTO, response.getBody().getData_nascimento());
        assertEquals(DATA_ATUALIZACAO, response.getBody().getData_atualizacao());
        assertEquals(PLANO, response.getBody().getPlano());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(cliente);

        ResponseEntity<Cliente> response = controller.create(clienteDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(cliente);

        ResponseEntity<Cliente> response = controller.update(ID, clienteDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Cliente.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ID, response.getBody().getId_cliente());
        assertEquals(NOME, response.getBody().getNome());
        assertEquals(SOBRENOME, response.getBody().getSobrenome());
        assertEquals(APELIDO, response.getBody().getApelido());
        assertEquals(CPF, response.getBody().getCpf());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(DATA_CADASTRO, response.getBody().getData_cadastro());
        assertEquals(DATA_NASCIMENTO, response.getBody().getData_nascimento());
        assertEquals(DATA_ATUALIZACAO, response.getBody().getData_atualizacao());
        assertEquals(PLANO, response.getBody().getPlano());
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
        cliente = new Cliente(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, PLANO, DATA_CADASTRO, DATA_ATUALIZACAO);
        clienteDTO = new ClienteDTO(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, 1L);
    }
}