package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.UsuarioDTO;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.enums.Perfil;
import br.janioofi.msgym.domain.repositories.ProfissionalRepository;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioServiceTest {

    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String USUARIO   = "TESTE";
    public static final String SENHA = "admin";
    public static final Set<Perfil> PERFIL = new HashSet<>(Collections.singleton(Perfil.ADMIN));

    private UsuarioDTO usuarioDTO;
    private Usuario usuario = new Usuario();
    private Optional<Usuario> optionalUser;

    @InjectMocks
    private UsuarioService service;
    @Mock
    private UsuarioRepository repository;
    @Mock
    private ProfissionalRepository profissionalRepository;
    private final ModelMapper mapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUsuario();
        service = new UsuarioService(repository, profissionalRepository);
    }

    @Test
    void whenFindByIdThenReturnAUsuarioInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalUser);
        UsuarioDTO response = service.findById(ID);
        assertNotNull(response);
        assertEquals(UsuarioDTO.class, response.getClass());
        assertEquals(ID, response.id_usuario());
        assertEquals(USUARIO, response.usuario());
        assertEquals(SENHA, response.senha());
        assertEquals(PERFIL, response.perfis());
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
    void whenFindAllThenReturnAListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(usuario));
        List<UsuarioDTO> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(UsuarioDTO.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).id_usuario());
        assertEquals(USUARIO, response.get(INDEX).usuario());
        assertEquals(SENHA, response.get(INDEX).senha());
        assertEquals(PERFIL, response.get(INDEX).perfis());

    }

    @Test
    void whenUpdateThenReturnAnRecordNotFoundException() {
        when(repository.save(any())).thenReturn(usuario);

        try {
            service.update(ID,usuarioDTO);
        }catch (Exception e){
            assertEquals(RecordNotFoundException.class, e.getClass());
        }
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(any())).thenReturn(optionalUser);
        doNothing().when(repository).deleteById(anyLong());
        service.delete(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    private void startUsuario(){
        usuario = new Usuario(ID, USUARIO, SENHA, PERFIL);
        usuarioDTO = new UsuarioDTO(ID, USUARIO, SENHA, PERFIL);
        optionalUser = Optional.of(new Usuario(ID, USUARIO, SENHA, PERFIL));
    }
}