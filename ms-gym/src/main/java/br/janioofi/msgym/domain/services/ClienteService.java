package br.janioofi.msgym.domain.services;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msgym.config.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ModelMapper mapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    public List<Cliente> findAll(){
        log.info("Listando todos os clientes");
        return repository.findAll();
    }

    public Cliente findById(Long id){
        log.info("Buscando por cliente com ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + id));
    }

    @Transactional
    public Cliente create(@Valid ClienteDTO clienteDTO){
        validaCpfEEmail(clienteDTO);
        log.info("Criando novo cliente: " + clienteDTO);
        Cliente cliente = new Cliente();
        Plano plano = planoRepository.findById(clienteDTO.getPlano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + clienteDTO.getPlano()));
        cliente.setNome(clienteDTO.getNome());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setPlano(plano);
        cliente.setData_cadastro(LocalDate.now());
        cliente.setData_nascimento(clienteDTO.getData_nascimento());
        cliente.setApelido(clienteDTO.getApelido());
        cliente.setSobrenome(clienteDTO.getSobrenome());
        repository.save(cliente);
        EmailDto email = new EmailDto();
        email.setEmailTo(cliente.getEmail());
        email.setSubject("Cadastro na Social Gym");
        email.setText("Seja-bem vindo a nossa academia, estamos muito contentes com chegada ao nosso time. Veja alguns detalhes do seu cadastro: "+
                "Plano: " + cliente.getPlano().getDescricao() +
                "Valor: R$ " + cliente.getPlano().getPreco() +
                "Data cadastro: " + cliente.getData_cadastro());
        producer.publishMessageEmail(email);
        return repository.save(cliente);
    }

    public void delete(Long id){
        log.info("Deletando cliente com  ID: " + id);
        repository.deleteById(id);
    }
    
    public Cliente update(Long id,@Valid ClienteDTO clienteDTO){
        validaCpfEEmail(clienteDTO);
        log.info("Atualizando cliente de ID: " + id + ", com informações: " + clienteDTO);
        Plano plano = planoRepository.findById(clienteDTO.getPlano()).orElseThrow(() ->  new RecordNotFoundException("Nenhum plano encontrado com o ID: " + clienteDTO.getPlano()));
        return repository.findById(id).map(data -> {
                    data.setNome(clienteDTO.getNome());
                    data.setCpf(clienteDTO.getCpf());
                    data.setApelido(clienteDTO.getApelido());
                    data.setSobrenome(clienteDTO.getSobrenome());
                    data.setEmail(clienteDTO.getEmail());
                    data.setData_atualizacao(LocalDateTime.now());
                    data.setData_nascimento(clienteDTO.getData_nascimento());
                    data.setPlano(plano);
                    return repository.save(data);
                }).orElseThrow(() -> new RecordNotFoundException("Nenhum cliente encontrado com o ID: " + id));
    }

    public void validaCpfEEmail(ClienteDTO clienteDTO){
        Optional<Cliente> obj = repository.findByCpf(clienteDTO.getCpf());

        if (obj.isPresent() && !obj.get().getId_cliente().equals(clienteDTO.getId_cliente())) {
            throw new DataIntegrityViolationException("CPF já está sendo utilizado");
        }

        obj = repository.findByEmail(clienteDTO.getEmail());
        if (obj.isPresent() && !obj.get().getId_cliente().equals(clienteDTO.getId_cliente())) {
            throw new DataIntegrityViolationException("E-mail já está sendo utilizado");
        }
    }

}
