package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Profissional;
import br.janioofi.msgym.domain.repositories.ProfissionalRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
        log.info("Novo profissional criado: " + profissional);
        return modelMapper.map(repository.save(modelMapper.map(profissional, Profissional.class)), ProfissionalDTO.class);
    }
}
