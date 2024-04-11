package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.PlanoDTO;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
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
public class PlanoService {

    private final PlanoRepository repository;
    private final ModelMapper modelMapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    public List<PlanoDTO> findAll(){
        log.info("Listando planos.");
        return repository.findAll().stream().map(object -> modelMapper.map(object, PlanoDTO.class)).toList();
    }

    public PlanoDTO findById(Long id){
        log.info("Buscando por planos com ID: " + id);
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum plano encontrado com o ID: " + id)), PlanoDTO.class);
    }

    public PlanoDTO create(PlanoDTO plano){
        validaDescricao(plano);
        log.info("Criando novo plano: " + plano);
        return modelMapper.map(repository.save(modelMapper.map(plano, Plano.class)), PlanoDTO.class);
    }

    public void delete(Long id){
        log.info("Deletando plano com  ID: " + id);
        repository.deleteById(id);
    }

    public PlanoDTO update(Long id, PlanoDTO plano){
        validaDescricao(plano);
        log.info("Atualizando plano com  ID:  " + id + ",  com as novas informações: " + plano);
        return modelMapper.map(
                repository.findById(id).map(recordFound -> {
                recordFound.setDescricao(plano.getDescricao());
                recordFound.setPreco(plano.getPreco());
                recordFound.setVigencia(plano.getVigencia());
                return modelMapper.map(repository.save(recordFound), PlanoDTO.class);
        }), PlanoDTO.class);
    }

    public void validaDescricao(PlanoDTO plano){
        Optional<Plano> obj = repository.findByDescricao(plano.getDescricao());

        if (obj.isPresent() && !obj.get().getId_plano().equals(plano.getId_plano())) {
            throw new DataIntegrityViolationException("Já existe um plano cadastrado com está descrição");
        }
    }
}
