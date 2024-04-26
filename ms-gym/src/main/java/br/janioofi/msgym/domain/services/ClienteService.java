package br.janioofi.msgym.domain.services;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
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

    public List<Cliente> findAll(){
        log.info("Listando todos os clientes");
        return repository.findAll();
    }

    public Cliente findById(Long id){
        log.info("Buscando por cliente com ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + id));
    }

    public Cliente create(@Valid ClienteDTO clienteDTO){
        validaCpfEEmail(clienteDTO);
        log.info("Criando novo cliente: " + clienteDTO);
        Cliente cliente = new Cliente();
        Plano plano = planoRepository.findById(clienteDTO.plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + clienteDTO.plano()));
        cliente.setNome(clienteDTO.nome());
        cliente.setCpf(clienteDTO.cpf());
        cliente.setEmail(clienteDTO.email());
        cliente.setPlano(plano);
        cliente.setData_cadastro(LocalDate.now());
        cliente.setData_nascimento(clienteDTO.data_nascimento());
        cliente.setApelido(clienteDTO.apelido());
        cliente.setSobrenome(clienteDTO.sobrenome());
        sendEmailCliente(cliente);
        return repository.save(cliente);
    }

    public void delete(Long id){
        log.info("Deletando cliente com  ID: " + id);
        repository.deleteById(id);
    }
    
    public Cliente update(Long id,@Valid ClienteDTO clienteDTO){
        validaCpfEEmail(clienteDTO);
        log.info("Atualizando cliente de ID: " + id + ", com informações: " + clienteDTO);
        Plano plano = planoRepository.findById(clienteDTO.plano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + clienteDTO.plano()));
        return repository.findById(id).map(data -> {
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
    }

    public void validaCpfEEmail(ClienteDTO clienteDTO){
        Optional<Cliente> obj = repository.findByCpf(clienteDTO.cpf());

        if (obj.isPresent() && !obj.get().getId_cliente().equals(clienteDTO.id_cliente())) {
            throw new DataIntegrityViolationException("CPF já está sendo utilizado");
        }

        obj = repository.findByEmail(clienteDTO.email());
        if (obj.isPresent() && !obj.get().getId_cliente().equals(clienteDTO.id_cliente())) {
            throw new DataIntegrityViolationException("E-mail já está sendo utilizado");
        }
    }

    public void sendEmailCliente(Cliente cliente){
        EmailDto email = new EmailDto(cliente.getEmail(), "Cadastro na Social Gym", "Seja-bem vindo a nossa academia, estamos muito contentes com sua chegada ao nosso time. \nVeja alguns detalhes do seu cadastro: "+
                "\nPlano: " + cliente.getPlano().getDescricao() +
                "\nValor: R$ " + cliente.getPlano().getPreco() +
                "\nData cadastro: " + cliente.getData_cadastro());
        producer.publishMessageEmail(email);
    }

}
