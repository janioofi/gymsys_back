package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.services.ProfissionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProfissionalControllerTest {

    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String NOME   = "admin";
    public static final String SOBRENOME = "admin";
    public static final String CPF = "72288363088";
    public static final String EMAIL = "janio@gmail.com";
    public static final LocalDate DATA_NASCIMENTO = LocalDate.of(2001, 9, 7);
    public static final LocalDate DATA_ADMISSAO = LocalDate.of(2024, 1, 1);
    public static final Usuario USUARIO = new Usuario();

    private ProfissionalDTO profissionalDTO;

    @InjectMocks
    private ProfissionalController controller;

    @Mock
    private ProfissionalService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startProfissional();
        controller = new ProfissionalController(service);
    }

    @Test
    void whenFindAllThenReturnListOfProfissionalDTO() {
        when(service.findAll()).thenReturn(List.of(profissionalDTO));

        ResponseEntity<List<ProfissionalDTO>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ProfissionalDTO.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).id_profissional());
        assertEquals(NOME, response.getBody().get(INDEX).nome());
        assertEquals(SOBRENOME, response.getBody().get(INDEX).sobrenome());
        assertEquals(CPF, response.getBody().get(INDEX).cpf());
        assertEquals(EMAIL, response.getBody().get(INDEX).email());
        assertEquals(DATA_ADMISSAO, response.getBody().get(INDEX).data_admissao());
        assertEquals(DATA_NASCIMENTO, response.getBody().get(INDEX).data_nascimento());
        assertEquals(USUARIO.getId_usuario(), response.getBody().get(INDEX).id_usuario());
        assertEquals(USUARIO.getUsuario(), response.getBody().get(INDEX).usuario());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(profissionalDTO);
        ResponseEntity<ProfissionalDTO> response = controller.findById(ID);
        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ProfissionalDTO.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().id_profissional());
        assertEquals(NOME, response.getBody().nome());
        assertEquals(SOBRENOME, response.getBody().sobrenome());
        assertEquals(CPF, response.getBody().cpf());
        assertEquals(EMAIL, response.getBody().email());
        assertEquals(DATA_ADMISSAO, response.getBody().data_admissao());
        assertEquals(DATA_NASCIMENTO, response.getBody().data_nascimento());
        assertEquals(USUARIO.getId_usuario(), response.getBody().id_usuario());
        assertEquals(USUARIO.getUsuario(), response.getBody().usuario());
    }

    @Test
    void whenCreateThenReturnCreated() {
        when(service.create(any())).thenReturn(profissionalDTO);

        ResponseEntity<ProfissionalDTO> response = controller.create(profissionalDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(profissionalDTO);

        ResponseEntity<ProfissionalDTO> response = controller.update(ID, profissionalDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ProfissionalDTO.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().id_profissional());
        assertEquals(NOME, response.getBody().nome());
        assertEquals(SOBRENOME, response.getBody().sobrenome());
        assertEquals(CPF, response.getBody().cpf());
        assertEquals(EMAIL, response.getBody().email());
        assertEquals(DATA_ADMISSAO, response.getBody().data_admissao());
        assertEquals(DATA_NASCIMENTO, response.getBody().data_nascimento());
        assertEquals(USUARIO.getId_usuario(), response.getBody().id_usuario());
        assertEquals(USUARIO.getUsuario(), response.getBody().usuario());
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

    private void startProfissional() {
        profissionalDTO = new ProfissionalDTO(ID, NOME, SOBRENOME, CPF, EMAIL, DATA_NASCIMENTO, DATA_ADMISSAO, USUARIO.getUsuario(), USUARIO.getId_usuario());
    }
}