package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class ClienteServiceTest {

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
    private ClienteDTO clienteDTO = new ClienteDTO();
    private Optional<Plano> plano;
    private Optional<Cliente> optionalCliente;

    @InjectMocks
    private ClienteService service;

    @Mock
    private ClienteRepository repository;
    @Mock
    private PlanoRepository planoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCliente();
        service = new ClienteService(repository, planoRepository);
    }

    @Test
    void whenFindByIdThenReturnAClienteInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalCliente);
        Cliente response = service.findById(ID);
        assertNotNull(response);
        assertEquals(Cliente.class, response.getClass());
        assertEquals(ID, response.getId_cliente());
        assertEquals(NOME, response.getNome());
        assertEquals(SOBRENOME, response.getSobrenome());
        assertEquals(APELIDO, response.getApelido());
        assertEquals(CPF, response.getCpf());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(DATA_CADASTRO, response.getData_cadastro());
        assertEquals(DATA_NASCIMENTO, response.getData_nascimento());
        assertEquals(DATA_ATUALIZACAO, response.getData_atualizacao());
        assertEquals(PLANO, response.getPlano());
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
    void whenFindAllThenReturnAListOfClientes() {
        when(repository.findAll()).thenReturn(List.of(cliente));
        List<Cliente> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Cliente.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId_cliente());
        assertEquals(NOME, response.get(INDEX).getNome());
        assertEquals(SOBRENOME, response.get(INDEX).getSobrenome());
        assertEquals(APELIDO, response.get(INDEX).getApelido());
        assertEquals(CPF, response.get(INDEX).getCpf());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(DATA_CADASTRO, response.get(INDEX).getData_cadastro());
        assertEquals(DATA_NASCIMENTO, response.get(INDEX).getData_nascimento());
        assertEquals(DATA_ATUALIZACAO, response.get(INDEX).getData_atualizacao());
        assertEquals(PLANO, response.get(INDEX).getPlano());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(planoRepository.findById(anyLong())).thenReturn(plano);
        when(repository.save(any())).thenReturn(cliente);

        Cliente response = service.create(clienteDTO);

        assertNotNull(response);
        assertEquals(Cliente.class, response.getClass());
        assertEquals(ID, response.getId_cliente());
        assertEquals(NOME, response.getNome());
        assertEquals(SOBRENOME, response.getSobrenome());
        assertEquals(APELIDO, response.getApelido());
        assertEquals(CPF, response.getCpf());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(DATA_CADASTRO, response.getData_cadastro());
        assertEquals(DATA_NASCIMENTO, response.getData_nascimento());
        assertEquals(DATA_ATUALIZACAO, response.getData_atualizacao());
        assertEquals(PLANO, response.getPlano());
    }

    @Test
    void whenCreateThenReturnAndDataIntegrityViolationException() {
        when(repository.findByCpf(anyString())).thenReturn(optionalCliente);
        try {
            optionalCliente.get().setId_cliente(2L);
            service.create(clienteDTO);
        }catch (Exception e){
            assertEquals(DataIntegrityViolationException.class, e.getClass());
        }
    }

    @Test
    void whenUpdateThenReturnAndDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalCliente);
        try {
            optionalCliente.get().setId_cliente(2L);
            service.create(clienteDTO);
        }catch (Exception e){
            assertEquals(DataIntegrityViolationException.class, e.getClass());
        }
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(any())).thenReturn(optionalCliente);
        doNothing().when(repository).deleteById(anyLong());
        service.delete(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void whenUpdateThenReturnAnRecordNotFoundException() {
        when(repository.save(any())).thenReturn(cliente);

        try {
            service.update(ID, clienteDTO);
        }catch (Exception e){
            assertEquals(RecordNotFoundException.class, e.getClass());
        }
    }

    private void startCliente() {
        cliente = new Cliente(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, PLANO, DATA_CADASTRO, DATA_ATUALIZACAO);
        optionalCliente = Optional.of(new Cliente(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, PLANO, DATA_CADASTRO, DATA_ATUALIZACAO));
        clienteDTO = new ClienteDTO(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, 1L);
        plano = Optional.of(new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20)));
    }
}