package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.dtos.EmailDto;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PagamentoRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.InvalidException;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteService {

    private final EmailProducer producer;
    private final ClienteRepository repository;
    private final PlanoRepository planoRepository;
    private final PagamentoRepository pagamentoRepository;

    public List<ClienteDTO> findAll(){
        log.info("Listando todos os clientes");
        List<Cliente> clientes = repository.findAll();
        return clientes.stream().map(this::mapToDTO).toList();
    }

    public ClienteDTO findById(Long id){
        log.info("Buscando por cliente com ID: " + id);
        Cliente cliente = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + id));
        return this.mapToDTO(cliente);
    }

    public ClienteDTO findByCPF(String cpf){
        log.info("Buscando por cliente com CPF: " + cpf);
        Cliente cliente = repository.findByCpf(cpf).orElseThrow(() -> new RecordNotFoundException("Nenhum cliente encontrado com o CPF: " + cpf));
        return this.mapToDTO(cliente);
    }

    public ClienteDTO create(@Valid ClienteDTO clienteDTO){
        validaCpfEEmail(clienteDTO);
        log.info("Criando novo cliente: " + clienteDTO);
        Cliente cliente = new Cliente();
        Plano plano = planoRepository.findById(clienteDTO.id_plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + clienteDTO.plano()));
        cliente.setNome(clienteDTO.nome());
        cliente.setCpf(clienteDTO.cpf());
        cliente.setEmail(clienteDTO.email());
        cliente.setPlano(plano);
        cliente.setData_cadastro(LocalDate.now());
        cliente.setData_nascimento(clienteDTO.data_nascimento());
        cliente.setApelido(clienteDTO.apelido());
        cliente.setSobrenome(clienteDTO.sobrenome());
        sendEmailCliente(cliente);
        return this.mapToDTO(repository.save(cliente));
    }

    public void delete(Long id){
        log.info("Deletando cliente com  ID: " + id);
        validaDelete(id);
        repository.deleteById(id);
    }
    
    public ClienteDTO update(Long id,@Valid ClienteDTO clienteDTO){
        validaCpfEEmail(clienteDTO);
        log.info("Atualizando cliente de ID: " + id + ", com informações: " + clienteDTO);
        Plano plano = planoRepository.findById(clienteDTO.id_plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + clienteDTO.plano()));
        Cliente cliente = repository.findById(id).map(data -> {
                    data.setNome(clienteDTO.nome());
                    data.setCpf(clienteDTO.cpf());
                    data.setApelido(clienteDTO.apelido());
                    data.setSobrenome(clienteDTO.sobrenome());
                    data.setEmail(clienteDTO.email());
                    data.setData_atualizacao(LocalDateTime.now());
                    data.setData_nascimento(clienteDTO.data_nascimento());
                    data.setPlano(plano);
                    return repository.save(data);
                }).orElseThrow(() -> new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + id));
        return this.mapToDTO(cliente);
    }

    private void validaCpfEEmail(ClienteDTO clienteDTO){
        Optional<Cliente> obj = repository.findByCpf(clienteDTO.cpf());

        if (obj.isPresent() && !obj.get().getId_cliente().equals(clienteDTO.id_cliente())) {
            throw new DataIntegrityViolationException("CPF já está sendo utilizado");
        }

        obj = repository.findByEmail(clienteDTO.email());
        if (obj.isPresent() && !obj.get().getId_cliente().equals(clienteDTO.id_cliente())) {
            throw new DataIntegrityViolationException("E-mail já está sendo utilizado");
        }
    }

    private void validaDelete(Long id_cliente){
        Optional<List<Pagamento>> pagamentos = pagamentoRepository.pagamentosPorCliente(id_cliente);
        if(pagamentos.get().size() > 0){
            throw new InvalidException("Cliente não pode ser deletado pois existe pagamento(s) associados a ele");
        }
    }

    private void sendEmailCliente(Cliente cliente){
        EmailDto email = new EmailDto(cliente.getEmail(), "Cadastro na Social Gym", "Seja-bem vinda(o) a nossa academia, estamos muito contentes com sua chegada ao nosso time. \nVeja alguns detalhes do seu cadastro: "+
                "\nPlano: " + cliente.getPlano().getDescricao() +
                "\nValor: R$ " + cliente.getPlano().getPreco() +
                "\nData cadastro: " + cliente.getData_cadastro());
        producer.publishMessageEmail(email);
    }

    private ClienteDTO mapToDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId_cliente(), cliente.getNome(), cliente.getSobrenome(), cliente.getApelido(), cliente.getCpf(),
        cliente.getEmail(), cliente.getData_nascimento(), cliente.getPlano().getDescricao(), cliente.getPlano().getId_plano());
    }
}
