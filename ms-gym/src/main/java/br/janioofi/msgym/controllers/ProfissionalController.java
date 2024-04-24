package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.ProfissionalDTO;
import br.janioofi.msgym.domain.entities.Profissional;
import br.janioofi.msgym.domain.services.ProfissionalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profissional", description = "API Profissional")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profissionais")
public class ProfissionalController {
    private final ProfissionalService service;

    @GetMapping
    public ResponseEntity<List<Profissional>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissional> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Profissional> create(@RequestBody ProfissionalDTO profissional){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(profissional));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profissional> update(@PathVariable Long id, @RequestBody ProfissionalDTO profissionalDTO){
        return ResponseEntity.ok().body(service.update(id, profissionalDTO));
    }
}
