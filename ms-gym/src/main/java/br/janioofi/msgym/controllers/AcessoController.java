package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.AcessoDTO;
import br.janioofi.msgym.domain.entities.Acesso;
import br.janioofi.msgym.domain.services.AcessoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acesso")
@Tag(name = "Acesso", description = "API Acesso")
@RequiredArgsConstructor
public class AcessoController {
    private final AcessoService service;

    @PostMapping
    public ResponseEntity<Acesso> entrada(@RequestBody AcessoDTO acessoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.entrada(acessoDTO));
    }

}
