package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PlanoDTO;
import br.janioofi.msgym.domain.entities.Plano;
import br.janioofi.msgym.domain.services.PlanoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Plano", description = "API Plano")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/planos")
public class PlanoController {
    private final PlanoService service;

    @GetMapping
    public ResponseEntity<List<Plano>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plano> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Plano> create(@RequestBody PlanoDTO plano){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(plano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plano> update(@PathVariable Long id,@RequestBody PlanoDTO plano){
        return ResponseEntity.ok().body(service.update(id, plano));
    }
}
