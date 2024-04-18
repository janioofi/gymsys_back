package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Profissional;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.repositories.ProfissionalRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.*;

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
    public static final Set<Usuario> USUARIO = new HashSet<>();

    private ProfissionalDTO profissionalDTO = new ProfissionalDTO();
    private Profissional profissional = new Profissional();
    private Optional<Profissional> optionalProfissional;

    @InjectMocks
    private ProfissionalService service;

    @Mock
    private ProfissionalRepository repository;
    private final ModelMapper mapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startProfissional();
        service = new ProfissionalService(repository);
    }

    @Test
    void whenFindByIdThenReturnAProfissionalInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalProfissional);
        Profissional response = mapper.map(service.findById(ID), Profissional.class);
        assertNotNull(response);
        assertEquals(Profissional.class, response.getClass());
        assertEquals(ID, response.getId_profissional());
        assertEquals(NOME, response.getNome());
        assertEquals(SOBRENOME, response.getSobrenome());
        assertEquals(CPF, response.getCpf());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(DATA_NASCIMENTO, response.getData_nascimento());
        assertEquals(DATA_ADMISSAO, response.getData_admissao());
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
        List<Profissional> response = service.findAll().stream().map(object  -> mapper.map(object, Profissional.class)).toList();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Profissional.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId_profissional());
        assertEquals(NOME, response.get(INDEX).getNome());
        assertEquals(SOBRENOME, response.get(INDEX).getSobrenome());
        assertEquals(CPF, response.get(INDEX).getCpf());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(DATA_NASCIMENTO, response.get(INDEX).getData_nascimento());
        assertEquals(DATA_ADMISSAO, response.get(INDEX).getData_admissao());

    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(profissional);
        Profissional response = mapper.map(service.create(profissionalDTO), Profissional.class);

        assertNotNull(response);
        assertEquals(Profissional.class, response.getClass());
        assertEquals(ID, response.getId_profissional());
        assertEquals(NOME, response.getNome());
        assertEquals(SOBRENOME, response.getSobrenome());
        assertEquals(CPF, response.getCpf());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(DATA_NASCIMENTO, response.getData_nascimento());
        assertEquals(DATA_ADMISSAO, response.getData_admissao());
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
        profissionalDTO = new ProfissionalDTO(ID, NOME, SOBRENOME, CPF, EMAIL, DATA_NASCIMENTO, DATA_ADMISSAO, USUARIO);
        optionalProfissional = Optional.of(new Profissional(ID, NOME, SOBRENOME, CPF, EMAIL, DATA_NASCIMENTO, DATA_ADMISSAO, USUARIO));
    }
}