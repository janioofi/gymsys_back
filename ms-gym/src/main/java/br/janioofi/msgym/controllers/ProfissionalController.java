package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.services.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profissionais")
public class ProfissionalController {
    private final ProfissionalService service;

    @GetMapping
    public ResponseEntity<List<ProfissionalDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProfissionalDTO> create(@RequestBody ProfissionalDTO profissional){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(profissional));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> update(@PathVariable Long id, @RequestBody ProfissionalDTO profissionalDTO){
        return ResponseEntity.ok().body(service.update(id, profissionalDTO));
    }
}
