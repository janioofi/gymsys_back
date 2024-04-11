package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Profissional;
import br.janioofi.msgym.domain.repositories.ProfissionalRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfissionalService {
    private final ProfissionalRepository repository;
    private final ModelMapper modelMapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    public List<ProfissionalDTO> findAll(){
        log.info("Listando profissionais.");
        return repository.findAll().stream().map(object -> modelMapper.map(object, ProfissionalDTO.class)).toList();
    }

    public ProfissionalDTO findById(Long id){
        log.info("Buscando por profissional com ID: " + id);
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum profissional encontrado com o ID: " + id)), ProfissionalDTO.class);
    }

    public ProfissionalDTO create(@Valid ProfissionalDTO profissional){
        validaPorCpfEEmail(profissional);
        log.info("Criando novo profissional: " + profissional);
        return modelMapper.map(repository.save(modelMapper.map(profissional, Profissional.class)), ProfissionalDTO.class);
    }

    public void delete(Long id){
        log.info("Deletando profissional com  ID: " + id);
        repository.deleteById(id);
    }

    public ProfissionalDTO update(Long id, ProfissionalDTO data){
        validaPorCpfEEmail(data);
        log.info("Atualizando profissional com  ID:  " + id + ",  com as novas informações: " + data);
        return modelMapper.map(
                repository.findById(id).map(recordFound -> {
                recordFound.setUsuarios(data.getUsuarios());
                recordFound.setNome(data.getNome());
                recordFound.setSobrenome(data.getSobrenome());
                recordFound.setCpf(data.getCpf());
                recordFound.setEmail(data.getEmail());
                recordFound.setData_nascimento(data.getData_nascimento());
                recordFound.setData_admissao(data.getData_admissao());
                return repository.save(modelMapper.map(recordFound, Profissional.class));
            }).orElseThrow(() -> new RecordNotFoundException("Nenhum profissional encontrado com o ID: " + id))
        , ProfissionalDTO.class);
    }

    private void validaPorCpfEEmail(ProfissionalDTO objDTO) {
        Optional<Profissional> obj = repository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && !obj.get().getId_profissional().equals(objDTO.getId_profissional())) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = repository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && !obj.get().getId_profissional().equals(objDTO.getId_profissional())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }
}
