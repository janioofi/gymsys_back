package br.janioofi.msgym.domain.services;

import br.janioofi.msgym.domain.dtos.LoginResponse;
import br.janioofi.msgym.domain.dtos.UsuarioLogin;
import br.janioofi.msgym.domain.dtos.UsuarioRegistro;
import br.janioofi.msgym.domain.entities.Usuario;
import br.janioofi.msgym.domain.enums.Perfil;
import br.janioofi.msgym.domain.repositories.UsuarioRepository;
import br.janioofi.msgym.exceptions.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository repository;
    private final TokenService tokenService;

    public LoginResponse login(@Valid UsuarioLogin user){
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.usuario(), user.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        log.info("Usuario " + user.usuario() + " efetuando login" );
        return new LoginResponse(token);
    }

    public String register(@Valid UsuarioRegistro user){
        Set<Perfil> perfis = new HashSet<>(user.perfis());
        if(this.repository.findByUsuario(user.usuario()).isPresent()){
            throw new BusinessException("Já existe um usuário cadastrado com o mesmo nome");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.senha());
        Usuario data = new Usuario(null, user.usuario(), encryptedPassword, perfis);
        repository.save(data);
        log.info("Novo usuario cadastrado: " + data);
        return "Usuario registrado com sucesso";
    }
}
