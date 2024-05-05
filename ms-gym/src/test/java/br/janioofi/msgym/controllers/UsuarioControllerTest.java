package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.UsuarioDTO;
import br.janioofi.msgym.domain.enums.Perfil;
import br.janioofi.msgym.domain.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioControllerTest {

    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String USUARIO   = "admin";
    public static final String SENHA = "admin";
    public static final Set<Perfil> PERFIL = new HashSet<>(Collections.singleton(Perfil.ADMIN));

    private UsuarioDTO usuarioDTO;

    @InjectMocks
    private UsuarioController controller;

    @Mock
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUsuario();
        controller = new UsuarioController(service);
    }

    @Test
    void whenFindAllThenReturnListOfUsuarioDTO() {
        when(service.findAll()).thenReturn(List.of(usuarioDTO));

        ResponseEntity<List<UsuarioDTO>> response = controller.findAll();
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UsuarioDTO.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).id_usuario());
        assertEquals(USUARIO, response.getBody().get(INDEX).usuario());
        assertEquals(SENHA, response.getBody().get(INDEX).senha());
        assertEquals(PERFIL, response.getBody().get(INDEX).perfis());
    }

    @Test
    void whenFindByIdThenReturnSuccess() {
        when(service.findById(anyLong())).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = controller.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(ID, response.getBody().id_usuario());
        assertEquals(USUARIO, response.getBody().usuario());
        assertEquals(SENHA, response.getBody().senha());
        assertEquals(PERFIL, response.getBody().perfis());
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.update(anyLong(), any())).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = controller.update(ID, usuarioDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UsuarioDTO.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(ID, response.getBody().id_usuario());
        assertEquals(USUARIO, response.getBody().usuario());
        assertEquals(SENHA, response.getBody().senha());
        assertEquals(PERFIL, response.getBody().perfis());

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

    private void startUsuario() {
        usuarioDTO = new UsuarioDTO(ID, USUARIO, SENHA, PERFIL);
    }
}