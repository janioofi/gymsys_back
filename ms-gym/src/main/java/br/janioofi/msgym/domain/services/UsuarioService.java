package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.UsuarioDTO;
import br.janioofi.msgym.domain.entities.Profissional;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.enums.Perfil;
import br.janioofi.msgym.domain.repositories.ProfissionalRepository;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.BusinessException;
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
    private final ProfissionalRepository profissionalRepository;

    public List<UsuarioDTO> findAll(){
        log.info("Listando usuários.");
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public UsuarioDTO findById(Long id){
        log.info("Buscando usuário com  ID: " + id);
        return this.mapToDTO(repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id)));
    }

    public void delete(Long id){
        validaDelete(id);
        log.info("Deletando usuário com  ID: " + id);
        repository.deleteById(id);
    }

    public UsuarioDTO update(@Valid Long id, UsuarioDTO user){
        log.info("Atualizando usuário com  ID:  " + id + ",  com as novas informações: " + user);
        validaUsuario(user);
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.senha());
        Set<Perfil> perfis = new HashSet<>(user.perfis());
        Usuario usuario = repository.findById(id).map(recordFound -> {
                recordFound.setUsuario(user.usuario());
                recordFound.setPerfis(perfis);
                recordFound.setSenha(encryptedPassword);
                return repository.save(recordFound);
                }).orElseThrow(() -> new RecordNotFoundException("Nenhum usuário encontrado com o ID: " + id));
        return this.mapToDTO(usuario);
    }

    private void validaUsuario(UsuarioDTO objDTO) {
        Optional<Usuario> obj = repository.findByUsuario(objDTO.usuario());

        if (obj.isPresent() && !obj.get().getId_usuario().equals(objDTO.id_usuario())) {
            throw new DataIntegrityViolationException("Usuário já existe no sistema!");
        }
    }

    private void validaDelete(Long id){
        Optional<Profissional> prof = profissionalRepository.findByUsuario(id);
        Optional<Usuario> usuario = repository.findById(id);

        if(prof.isPresent()){
            throw new BusinessException("Existe um profissional cadastrado com este usuário");
        }
        if(usuario.isPresent() && usuario.get().getUsuario().equalsIgnoreCase("admin")){
            throw new BusinessException("Usuário admin não pode ser deletado");
        }else if(usuario.isEmpty()){
            throw new RecordNotFoundException("Nenhum usuário com este ID");
        }
    }

    private UsuarioDTO mapToDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId_usuario(), usuario.getUsuario(), usuario.getSenha(), usuario.getPerfis());
    }
}
