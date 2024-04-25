package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.PlanoDTO;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlanoServiceTest {
    private static final Integer INDEX   = 0;
    public static final Long ID = 1L;
    private static final String DESCRICAO   = "MENSAL";
    public static final LocalDate VIGENCIA = LocalDate.now();
    public static final BigDecimal PRECO = new BigDecimal("90.2");
    private static final Long QTD = 1L;

    private PlanoDTO planoDTO;
    private Plano plano = new Plano();
    private Optional<Plano> optionalPlano;

    @InjectMocks
    private PlanoService service;

    @Mock
    private PlanoRepository repository;
    private final ModelMapper mapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPlano();
        service = new PlanoService(repository);
    }

    @Test
    void whenFindByIdThenReturnAPlanoInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalPlano);
        Plano response = mapper.map(service.findById(ID), Plano.class);
        assertNotNull(response);
        assertEquals(Plano.class, response.getClass());
        assertEquals(ID, response.getId_plano());
        assertEquals(DESCRICAO, response.getDescricao());
        assertEquals(VIGENCIA, response.getVigencia());
        assertEquals(PRECO, response.getPreco());
        assertEquals(QTD, response.getQuantidadeMeses());
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
    void whenFindAllThenReturnAListOfPlanos() {
        when(repository.findAll()).thenReturn(List.of(plano));
        List<Plano> response = service.findAll().stream().map(object  -> mapper.map(object, Plano.class)).toList();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Plano.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId_plano());
        assertEquals(DESCRICAO, response.get(INDEX).getDescricao());
        assertEquals(VIGENCIA, response.get(INDEX).getVigencia());
        assertEquals(PRECO, response.get(INDEX).getPreco());
        assertEquals(QTD, response.get(INDEX).getQuantidadeMeses());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(plano);
        Plano response = mapper.map(service.create(planoDTO), Plano.class);

        assertNotNull(response);
        assertEquals(Plano.class, response.getClass());
        assertEquals(ID, response.getId_plano());
        assertEquals(DESCRICAO, response.getDescricao());
        assertEquals(VIGENCIA, response.getVigencia());
        assertEquals(PRECO, response.getPreco());
        assertEquals(QTD, response.getQuantidadeMeses());
    }

    @Test
    void whenCreateThenReturnAndDataIntegrityViolationException() {
        when(repository.findByDescricao(anyString())).thenReturn(optionalPlano);
        try {
            optionalPlano.get().setId_plano(2L);
            service.create(planoDTO);
        }catch (Exception e){
            assertEquals(DataIntegrityViolationException.class, e.getClass());
        }
    }

    @Test
    void whenUpdateThenReturnAnRecordNotFoundException() {
        when(repository.save(any())).thenReturn(plano);

        try {
            service.update(ID,planoDTO);
        }catch (Exception e){
            assertEquals(RecordNotFoundException.class, e.getClass());
        }
    }

    @Test
    void whenUpdateThenReturnAndDataIntegrityViolationException() {
        when(repository.findByDescricao(anyString())).thenReturn(optionalPlano);
        try {
            optionalPlano.get().setId_plano(2L);
            service.create(planoDTO);
        }catch (Exception e){
            assertEquals(DataIntegrityViolationException.class, e.getClass());
        }
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(any())).thenReturn(optionalPlano);
        doNothing().when(repository).deleteById(anyLong());
        service.delete(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    private void startPlano(){
        plano = new Plano(ID, DESCRICAO, VIGENCIA, PRECO, QTD);
        planoDTO = new PlanoDTO(ID, DESCRICAO, VIGENCIA, PRECO, QTD);
        optionalPlano = Optional.of(new Plano(ID, DESCRICAO, VIGENCIA, PRECO, QTD));
    }
}