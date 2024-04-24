package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.UsuarioDTO;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.enums.Perfil;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.RecordNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public List<Usuario> findAll(){
        log.info("Listando usuários.");
        return repository.findAll();
    }

    public Usuario findById(Long id){
        log.info("Buscando usuário com  ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id));
    }

    public void delete(Long id){
        log.info("Deletando usuário com  ID: " + id);
        repository.deleteById(id);
    }

    public Usuario update(@Valid Long id, UsuarioDTO user){
        log.info("Atualizando usuário com  ID:  " + id + ",  com as novas informações: " + user);
        validaUsuario(user);
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.senha());
        Set<Perfil> perfis = new HashSet<>(user.perfis());
        return repository.findById(id).map(recordFound -> {
                recordFound.setUsuario(user.usuario());
                recordFound.setPerfis(perfis);
                recordFound.setSenha(encryptedPassword);
                return repository.save(recordFound);
                }).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id));
    }

    private void validaUsuario(UsuarioDTO objDTO) {
        Optional<Usuario> obj = repository.findByUsuario(objDTO.usuario());

        if (obj.isPresent() && !obj.get().getId_usuario().equals(objDTO.id_usuario())) {
            throw new DataIntegrityViolationException("Usuário já existe no sistema!");
        }
    }

}
