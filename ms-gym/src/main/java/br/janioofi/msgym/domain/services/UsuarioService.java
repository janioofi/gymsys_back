package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.DTOS.UsuarioDTO;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ModelMapper modelMapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    public UsuarioDTO findById(Long id){
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id)), UsuarioDTO.class);
    }

    public UsuarioDTO create(UsuarioDTO user){
        return modelMapper.map(repository.save(modelMapper.map(user, Usuario.class)), UsuarioDTO.class);
    }

    public List<UsuarioDTO> findAll(){
        List<UsuarioDTO> usuarios;
        usuarios = repository.findAll().stream().map(object -> modelMapper.map(object, UsuarioDTO.class)).toList();
        return usuarios;
    }
}
