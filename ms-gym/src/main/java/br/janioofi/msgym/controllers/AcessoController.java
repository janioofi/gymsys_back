package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.AcessoDTO;
import br.janioofi.msgym.domain.dtos.AcessoResponseDTO;
import br.janioofi.msgym.domain.services.AcessoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acesso")
@Tag(name = "Acesso", description = "API Acesso")
@RequiredArgsConstructor
public class AcessoController {
    private final AcessoService service;

    @PostMapping("/entrada")
    public ResponseEntity<AcessoResponseDTO> entrada(@RequestBody AcessoDTO acessoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.entrada(acessoDTO));
    }

    @PostMapping("/saida")
    public ResponseEntity<AcessoResponseDTO> saida(@RequestBody AcessoDTO acessoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saida(acessoDTO));
    }

    @GetMapping("/diario")
    public ResponseEntity<List<AcessoResponseDTO>> findAllAcessosDoDia(){
        return ResponseEntity.ok().body(service.findAllAcessosDoDia());
    }

    @GetMapping()
    public ResponseEntity<List<AcessoResponseDTO>> findAllAcessosPeriodo(@RequestParam String data_inicio, @RequestParam String data_final){
        return ResponseEntity.ok().body(service.findAllAcessosPeriodo(data_inicio, data_final));
    }

    @GetMapping("/presentes")
    public ResponseEntity<List<AcessoResponseDTO>> treinandoAgora(){
        return ResponseEntity.ok().body(service.treinandoAgora());
    }

}
