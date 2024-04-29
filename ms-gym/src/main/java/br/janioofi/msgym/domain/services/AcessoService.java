package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.AcessoDTO;
import br.janioofi.msgym.domain.dtos.AcessoResponseDTO;
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

    public AcessoResponseDTO entrada(AcessoDTO acessoDTO){
        Acesso acesso = new Acesso();
        Cliente cliente = clienteRepository.findByCpf(acessoDTO.cpf()).orElseThrow(() -> new RecordNotFoundException("Não existe nenhum cadastro com este CPF"));
        acesso.setCliente(cliente);
        acesso.setCpf(acessoDTO.cpf());
        acesso.setData_registro(LocalDateTime.now());
        acesso.setTipoRegistro(TipoRegistro.valueOf("ENTRADA"));
        validacaoCliente.verificaEntrada(acesso.getCliente().getId_cliente());
        AcessoResponseDTO response = new AcessoResponseDTO(cliente.getNome(), cliente.getCpf(), acesso.getData_registro(), acesso.getTipoRegistro());
        repository.save(acesso);
        return response;
    }

    public AcessoResponseDTO saida(AcessoDTO acessoDTO){
        Acesso acesso = new Acesso();
        Cliente cliente = clienteRepository.findByCpf(acessoDTO.cpf()).orElseThrow(() -> new RecordNotFoundException("Não existe nenhum cadastro com este CPF"));
        acesso.setCliente(cliente);
        acesso.setCpf(acessoDTO.cpf());
        acesso.setData_registro(LocalDateTime.now());
        acesso.setTipoRegistro(TipoRegistro.valueOf("SAIDA"));
        validacaoCliente.verificaSaida(acesso.getCliente().getId_cliente());
        AcessoResponseDTO response = new AcessoResponseDTO(cliente.getNome(), cliente.getCpf(), acesso.getData_registro(), acesso.getTipoRegistro());
        repository.save(acesso);
        return response;
    }

    public List<AcessoResponseDTO> findAllAcessosDoDia() {
        LocalDateTime dataAtual = LocalDateTime.now();
        List<Acesso> acessosDoDia = repository.findAllAcessosDoDia(dataAtual.toString());
        return acessosDoDia.stream()
                .map(this::mapToDTO).toList();
    }

    public List<AcessoResponseDTO> findAllAcessosPeriodo(String data_inicio, String data_final){
        List<Acesso> acessoPeriodo = repository.findAllAcessosPeriodo(data_inicio, data_final);
        return acessoPeriodo.stream()
                .map(this::mapToDTO).toList();
    }

    public List<AcessoResponseDTO> treinandoAgora(){
        List<Acesso> treinandoAgora = repository.treinandoAgora(LocalDateTime.now().toString());
        return treinandoAgora.stream()
                .map(this::mapToDTO).toList();
    }

    private AcessoResponseDTO mapToDTO(Acesso acesso) {
        return new AcessoResponseDTO(
                acesso.getCliente().getNome(),  // Supondo que o cliente tem um campo 'nome'
                acesso.getCpf(),
                acesso.getData_registro(),
                acesso.getTipoRegistro()
        );
    }


}
