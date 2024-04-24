package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.PlanoDTO;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.repositories.PlanoRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository repository;

    public List<Plano> findAll(){
        log.info("Listando planos.");
        return repository.findAll();
    }

    public Plano findById(Long id){
        log.info("Buscando por planos com ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum plano encontrado com o ID: " + id));
    }

    public Plano create(PlanoDTO planoDTO){
        validaDescricao(planoDTO);
        log.info("Criando novo plano: " + planoDTO);
        Plano plano = new Plano();
        plano.setPreco(planoDTO.preco());
        plano.setVigencia(planoDTO.vigencia());
        plano.setDescricao(planoDTO.descricao());
        plano.setQuantidadeMeses(planoDTO.quantidadeMeses());
        return repository.save(plano);
    }

    public void delete(Long id){
        log.info("Deletando plano com  ID: " + id);
        repository.deleteById(id);
    }

    public Plano update(Long id, PlanoDTO plano){
        validaDescricao(plano);
        log.info("Atualizando plano com  ID:  " + id + ",  com as novas informações: " + plano);
        return repository.findById(id).map(recordFound -> {
                recordFound.setDescricao(plano.descricao());
                recordFound.setPreco(plano.preco());
                recordFound.setVigencia(plano.vigencia());
                recordFound.setQuantidadeMeses(plano.quantidadeMeses());
                return repository.save(recordFound);
        }).orElseThrow(() -> new RecordNotFoundException("Nenhum plano encontrado com o ID: " + id));
    }

    public void validaDescricao(PlanoDTO plano){
        Optional<Plano> obj = repository.findByDescricao(plano.descricao());

        if (obj.isPresent() && !obj.get().getId_plano().equals(plano.id_plano())) {
            throw new DataIntegrityViolationException("Já existe um plano cadastrado com está descrição");
        }
    }
}
