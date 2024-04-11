package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.UsuarioDTO;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ModelMapper modelMapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    public List<UsuarioDTO> findAll(){
        log.info("Listando usuários.");
        List<UsuarioDTO> usuarios;
        usuarios = repository.findAll().stream().map(object -> modelMapper.map(object, UsuarioDTO.class)).toList();
        return usuarios;
    }

    public UsuarioDTO findById(Long id){
        log.info("Buscando usuário com  ID: " + id);
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id)), UsuarioDTO.class);
    }

    public UsuarioDTO create(@Valid UsuarioDTO user){
        log.info("Novo usuário criado: " + user);
        return modelMapper.map(repository.save(modelMapper.map(user, Usuario.class)), UsuarioDTO.class);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public UsuarioDTO update(@Valid Long id, UsuarioDTO user){
        validaUsuario(user);
        return modelMapper.map(
                repository.findById(id).map(recordFound -> {
                recordFound.setUsuario(user.getUsuario());
                recordFound.setPerfil(user.getPerfil());
                recordFound.setSenha(user.getSenha());
                return repository.save(modelMapper.map(recordFound, Usuario.class));
                }).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id))
                , UsuarioDTO.class);
    }

    private void validaUsuario(UsuarioDTO objDTO) {
        Optional<Usuario> obj = repository.findByUsuario(objDTO.getUsuario());

        if (obj.isPresent() && obj.get().getId_usuario() != objDTO.getId_usuario()) {
            throw new DataIntegrityViolationException("Usuário já existe no sistema!");
        }
    }

}
