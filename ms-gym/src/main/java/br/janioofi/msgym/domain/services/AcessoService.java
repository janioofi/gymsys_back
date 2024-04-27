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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcessoService {

    private final AcessoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ValidacaoAcesso validacaoCliente;

    public Acesso entrada(AcessoDTO acessoDTO){
        Acesso acesso = new Acesso();
        Cliente cliente = clienteRepository.findByCpf(acessoDTO.cpf()).orElseThrow(() -> new RecordNotFoundException("Não existe nenhum cadastro com este CPF"));
        acesso.setCliente(cliente);
        acesso.setCpf(acessoDTO.cpf());
        acesso.setData_registro(LocalDateTime.now());
        acesso.setTipoRegistro(TipoRegistro.valueOf("ENTRADA"));
        validacaoCliente.verificaEntrada(acesso.getCliente().getId_cliente());
        return repository.save(acesso);
    }

    public Acesso saida(AcessoDTO acessoDTO){
        Acesso acesso = new Acesso();
        Cliente cliente = clienteRepository.findByCpf(acessoDTO.cpf()).orElseThrow(() -> new RecordNotFoundException("Não existe nenhum cadastro com este CPF"));
        acesso.setCliente(cliente);
        acesso.setCpf(acessoDTO.cpf());
        acesso.setData_registro(LocalDateTime.now());
        acesso.setTipoRegistro(TipoRegistro.valueOf("SAIDA"));
        validacaoCliente.verificaSaida(acesso.getCliente().getId_cliente());
        return repository.save(acesso);
    }

    public List<Acesso> findAllAcessosDoDia(){
        return repository.findAllAcessosDoDia(LocalDateTime.now().toString());
    }

    public List<Acesso> findAllAcessosPeriodo(String data_inicio, String data_final){
        return repository.findAllAcessosPeriodo(data_inicio, data_final);
    }

    public List<Acesso> treinandoAgora(){
        return repository.treinandoAgora(LocalDateTime.now().toString());
    }


}
