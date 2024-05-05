package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
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
    public static final Plano PLANO = new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20), 1L);

    private Cliente cliente = new Cliente();
    private ClienteDTO clienteDTO;
    private Optional<Plano> plano;
    private Optional<Cliente> optionalCliente;

    @InjectMocks
    private ClienteService service;

    @Mock
    private ClienteRepository repository;
    @Mock
    private PlanoRepository planoRepository;
    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private EmailProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCliente();
        service = new ClienteService(producer, repository, planoRepository, pagamentoRepository);
    }

    @Test
    void whenFindByIdThenReturnAClienteInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalCliente);
        ClienteDTO response = service.findById(ID);
        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.getClass());
        assertEquals(ID, response.id_cliente());
        assertEquals(NOME, response.nome());
        assertEquals(SOBRENOME, response.sobrenome());
        assertEquals(APELIDO, response.apelido());
        assertEquals(CPF, response.cpf());
        assertEquals(EMAIL, response.email());
        assertEquals(DATA_NASCIMENTO, response.data_nascimento());
        assertEquals(PLANO.getDescricao(), response.plano());
        assertEquals(PLANO.getId_plano(), response.id_plano());
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
        List<ClienteDTO> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ClienteDTO.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).id_cliente());
        assertEquals(NOME, response.get(INDEX).nome());
        assertEquals(SOBRENOME, response.get(INDEX).sobrenome());
        assertEquals(APELIDO, response.get(INDEX).apelido());
        assertEquals(CPF, response.get(INDEX).cpf());
        assertEquals(EMAIL, response.get(INDEX).email());
        assertEquals(DATA_NASCIMENTO, response.get(INDEX).data_nascimento());
        assertEquals(PLANO.getDescricao(), response.get(INDEX).plano());
        assertEquals(PLANO.getId_plano(), response.get(INDEX).id_plano());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(planoRepository.findById(anyLong())).thenReturn(plano);
        when(repository.save(any())).thenReturn(cliente);

        ClienteDTO response = service.create(clienteDTO);

        assertNotNull(response);
        assertEquals(ClienteDTO.class, response.getClass());
        assertEquals(ID, response.id_cliente());
        assertEquals(NOME, response.nome());
        assertEquals(SOBRENOME, response.sobrenome());
        assertEquals(APELIDO, response.apelido());
        assertEquals(CPF, response.cpf());
        assertEquals(EMAIL, response.email());
        assertEquals(DATA_NASCIMENTO, response.data_nascimento());
        assertEquals(PLANO.getDescricao(), response.plano());
        assertEquals(PLANO.getId_plano(), response.id_plano());
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
        clienteDTO = new ClienteDTO(ID, NOME, SOBRENOME, APELIDO, CPF, EMAIL, DATA_NASCIMENTO, PLANO.getDescricao(),PLANO.getId_plano());
        plano = Optional.of(new Plano(1L, "Teste",  LocalDate.now(), BigDecimal.valueOf(20), 1L));
    }
}