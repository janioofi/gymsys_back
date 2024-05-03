package br.janioofi.msgym.domain.services;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msgym.configs.producer.EmailProducer;
import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Profissional;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.repositories.ProfissionalRepository;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfissionalService {

    private final EmailProducer producer;
    private final ProfissionalRepository repository;
    private final UsuarioRepository usuarioRepository;

    public List<ProfissionalDTO> findAll(){
        List<Profissional> profissionais = repository.findAll();
        log.info("Listando profissionais.");
        return profissionais.stream().map(this::mapToDTO).toList();
    }

    public ProfissionalDTO findById(Long id){
        log.info("Buscando por profissional com ID: " + id);
        Profissional prof = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum profissional encontrado com o ID: " + id));
        return this.mapToDTO(prof);
    }

    public ProfissionalDTO create(@Valid ProfissionalDTO profissionalDTO){
        validaPorCpfEEmail(profissionalDTO);
        validaUsuarioCreate(profissionalDTO);
        log.info("Criando novo profissional: " + profissionalDTO);
        Profissional profissional = new Profissional();
        Usuario usuario = usuarioRepository.findById(profissionalDTO.id_usuario()).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + profissionalDTO.id_profissional()));
        profissional.setUsuario(usuario);
        profissional.setCpf(profissionalDTO.cpf());
        profissional.setSobrenome(profissionalDTO.sobrenome());
        profissional.setEmail(profissionalDTO.email());
        profissional.setData_nascimento(profissionalDTO.data_nascimento());
        profissional.setData_admissao(profissionalDTO.data_admissao());
        profissional.setNome(profissionalDTO.nome());
        sendEmailProfissional(profissional);
        Profissional id = repository.save(profissional);
        return this.mapToDTO(id);
    }

    public void delete(Long id){
        log.info("Deletando profissional com  ID: " + id);
        repository.deleteById(id);
    }

    public ProfissionalDTO update(Long id, ProfissionalDTO data){
        validaPorCpfEEmail(data);
        validaUsuarioUpdate(data);
        Usuario usuario = usuarioRepository.findById(data.id_usuario()).orElseThrow(() -> new RecordNotFoundException("Nenhum usuario encontrado com o ID: " + id));
        log.info("Atualizando profissional com  ID:  " + id + ",  com as novas informações: " + data);
        Profissional prof = repository.findById(id).map(recordFound -> {
                recordFound.setUsuario(usuario);
                recordFound.setNome(data.nome());
                recordFound.setSobrenome(data.sobrenome());
                recordFound.setCpf(data.cpf());
                recordFound.setEmail(data.email());
                recordFound.setData_nascimento(data.data_nascimento());
                recordFound.setData_admissao(data.data_nascimento());
                return repository.save(recordFound);
            }).orElseThrow(() -> new RecordNotFoundException("Nenhum profissional encontrado com o ID: " + id));
        return this.mapToDTO(prof);
    }

    private void validaPorCpfEEmail(ProfissionalDTO objDTO) {
        Optional<Profissional> obj = repository.findByCpf(objDTO.cpf());
        if (obj.isPresent() && !obj.get().getId_profissional().equals(objDTO.id_profissional())) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = repository.findByEmail(objDTO.email());
        if (obj.isPresent() && !obj.get().getId_profissional().equals(objDTO.id_profissional())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }

    private void validaUsuarioCreate(ProfissionalDTO profissionalDTO){
        Optional<Profissional> obj = repository.findByUsuario(profissionalDTO.id_profissional());
        if (obj.isPresent()) {
            throw new DataIntegrityViolationException("Usuário já está vinculado a um profissional!");
        }
    }

    private void validaUsuarioUpdate(ProfissionalDTO profissionalDTO){
        Optional<Profissional> obj = repository.findByUsuario(profissionalDTO.id_usuario());
        if (obj.isPresent() && !obj.get().getId_profissional().equals(profissionalDTO.id_profissional())) {
            throw new DataIntegrityViolationException("Usuário já está vinculado a um profissional!");
        }
    }

    public void sendEmailProfissional(Profissional profissional){
        EmailDto email = new EmailDto(profissional.getEmail(), "Novo integrante do time Gym Sys", "Seja-bem vinda(o) ao nosso time de profissionais da Gym Sys, ficamos muito felizes com sua entrada e te desejamos muito sucesso nessa nova jornada. \nVeja alguns detalhes do seu cadastro: "+
                "\nData de admissão: " + profissional.getData_admissao() +
                "\nNome: " + profissional.getNome() + " " + profissional.getSobrenome() +
                "\nData de nascimento: " + profissional.getData_nascimento() +
                "\nUsuário registrado: " + profissional.getUsuario().getUsuario());
        producer.publishMessageEmail(email);
    }

    private ProfissionalDTO mapToDTO(Profissional profissional) {
        return new ProfissionalDTO(profissional.getId_profissional(), profissional.getNome(), profissional.getSobrenome(), profissional.getCpf(), profissional.getEmail(),
                profissional.getData_nascimento(),
                profissional.getData_admissao(), profissional.getUsuario().getUsuario(), profissional.getUsuario().getId_usuario());
    }
}
