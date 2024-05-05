package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Profissional;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProfissionalServiceTest {
    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String NOME   = "admin";
    public static final String SOBRENOME = "admin";
    public static final String CPF = "72288363088";
    public static final String EMAIL = "janio@gmail.com";
    public static final LocalDate DATA_NASCIMENTO = LocalDate.of(2001, 9, 7);
    public static final LocalDate DATA_ADMISSAO = LocalDate.of(2024, 1, 1);
    public static final Usuario USUARIO = new Usuario(1L, "Teste", "teste", new HashSet<>(Collections.singleton(Perfil.ADMIN)));

    private ProfissionalDTO profissionalDTO;
    private Profissional profissional = new Profissional();
    private Optional<Usuario> optionalUsuario;
    private Optional<Profissional> optionalProfissional;

    @InjectMocks
    private ProfissionalService service;

    @Mock
    private ProfissionalRepository repository;

    @Mock
    private EmailProducer producer;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startProfissional();
        service = new ProfissionalService(producer,repository, usuarioRepository);
    }

    @Test
    void whenFindByIdThenReturnAProfissionalInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalProfissional);
        ProfissionalDTO response = service.findById(ID);
        assertNotNull(response);
        assertEquals(ProfissionalDTO.class, response.getClass());
        assertEquals(ID, response.id_profissional());
        assertEquals(NOME, response.nome());
        assertEquals(SOBRENOME, response.sobrenome());
        assertEquals(CPF, response.cpf());
        assertEquals(EMAIL, response.email());
        assertEquals(DATA_NASCIMENTO, response.data_nascimento());
        assertEquals(DATA_ADMISSAO, response.data_admissao());
        assertEquals(USUARIO.getId_usuario(), response.id_usuario());
        assertEquals(USUARIO.getUsuario(), response.usuario());
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
    void whenFindAllThenReturnAListOfProfissionais() {
        when(repository.findAll()).thenReturn(List.of(profissional));
        List<ProfissionalDTO> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ProfissionalDTO.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).id_profissional());
        assertEquals(NOME, response.get(INDEX).nome());
        assertEquals(SOBRENOME, response.get(INDEX).sobrenome());
        assertEquals(CPF, response.get(INDEX).cpf());
        assertEquals(EMAIL, response.get(INDEX).email());
        assertEquals(DATA_NASCIMENTO, response.get(INDEX).data_nascimento());
        assertEquals(DATA_ADMISSAO, response.get(INDEX).data_admissao());
        assertEquals(USUARIO.getUsuario(), response.get(INDEX).usuario());
        assertEquals(USUARIO.getId_usuario(), response.get(INDEX).id_usuario());

    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(usuarioRepository.findById(anyLong())).thenReturn(optionalUsuario);
        when(repository.save(any())).thenReturn(profissional);
        ProfissionalDTO response = service.create(profissionalDTO);

        assertNotNull(response);
        assertEquals(ProfissionalDTO.class, response.getClass());
        assertEquals(ID, response.id_profissional());
        assertEquals(NOME, response.nome());
        assertEquals(SOBRENOME, response.sobrenome());
        assertEquals(CPF, response.cpf());
        assertEquals(EMAIL, response.email());
        assertEquals(DATA_NASCIMENTO, response.data_nascimento());
        assertEquals(DATA_ADMISSAO, response.data_admissao());
        assertEquals(USUARIO.getId_usuario(), response.id_usuario());
        assertEquals(USUARIO.getUsuario(), response.usuario());
    }

    @Test
    void whenCreateThenReturnAndDataIntegrityViolationException() {
        when(repository.findByCpf(anyString())).thenReturn(optionalProfissional);
        try {
            optionalProfissional.get().setId_profissional(2L);
            service.create(profissionalDTO);
        }catch (Exception e){
            assertEquals(DataIntegrityViolationException.class, e.getClass());
        }
    }

    @Test
    void whenUpdateThenReturnAnRecordNotFoundException() {
        when(repository.save(any())).thenReturn(profissional);

        try {
            service.update(ID,profissionalDTO);
        }catch (Exception e){
            assertEquals(RecordNotFoundException.class, e.getClass());
        }
    }

    @Test
    void whenUpdateThenReturnAndDataIntegrityViolationException() {
        when(repository.findByCpf(anyString())).thenReturn(optionalProfissional);
        try {
            optionalProfissional.get().setId_profissional(2L);
            service.create(profissionalDTO);
        }catch (Exception e){
            assertEquals(DataIntegrityViolationException.class, e.getClass());
        }
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(any())).thenReturn(optionalProfissional);
        doNothing().when(repository).deleteById(anyLong());
        service.delete(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    private void startProfissional(){
        profissional = new Profissional(ID, NOME, SOBRENOME, CPF, EMAIL, DATA_NASCIMENTO, DATA_ADMISSAO, USUARIO);
        profissionalDTO = new ProfissionalDTO(ID, NOME, SOBRENOME, CPF, EMAIL, DATA_NASCIMENTO, DATA_ADMISSAO, USUARIO.getUsuario(), USUARIO.getId_usuario());
        optionalProfissional = Optional.of(new Profissional(ID, NOME, SOBRENOME, CPF, EMAIL, DATA_NASCIMENTO, DATA_ADMISSAO, USUARIO));
        optionalUsuario = Optional.of(new Usuario(1L, "Teste", "teste", new HashSet<>(Collections.singleton(Perfil.ADMIN))));
    }
}