package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.DTOS.UsuarioDTO;
import br.janioofi.msgym.domain.entities.Usuarios;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    private ModelMapper modelMapper = new ModelMapper().registerModule(new org.modelmapper.record.RecordModule());

    public UsuarioDTO findById(Long id){
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id)), UsuarioDTO.class);
    }

    public UsuarioDTO create(UsuarioDTO user){
        return modelMapper.map(repository.save(modelMapper.map(user, Usuarios.class)), UsuarioDTO.class);
    }

    public List<UsuarioDTO> findAll(){
        List<UsuarioDTO> usuarios = new ArrayList<>();
        usuarios = repository.findAll().stream().map(object -> {
            return modelMapper.map(object, UsuarioDTO.class);
        }).collect(Collectors.toList());
        return usuarios;
    }
}
