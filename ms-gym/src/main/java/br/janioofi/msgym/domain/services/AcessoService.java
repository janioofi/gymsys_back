package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.AcessoDTO;
import br.janioofi.msgym.domain.entities.Acesso;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.enums.TipoRegistro;
import br.janioofi.msgym.domain.repositories.AcessoRepository;
import br.janioofi.msgym.domain.repositories.ClienteRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AcessoService {
    private final AcessoRepository repository;
    private final ClienteRepository clienteRepository;

    public Acesso entrada(AcessoDTO acessoDTO){
        Acesso acesso = new Acesso();
        Cliente cliente = clienteRepository.findByCpf(acessoDTO.cpf()).orElseThrow(() -> new RecordNotFoundException("Não existe nenhum cadastro com este CPF"));
        acesso.setCliente(cliente);
        acesso.setCpf(acessoDTO.cpf());
        acesso.setData_registro(LocalDateTime.now());
        acesso.setTipoRegistro(TipoRegistro.valueOf("ENTRADA"));
        return repository.save(acesso);
    }

}
