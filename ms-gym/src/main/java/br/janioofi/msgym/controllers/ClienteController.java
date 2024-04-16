package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.ClienteDTO;
import br.janioofi.msgym.domain.entities.Cliente;
import br.janioofi.msgym.domain.services.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cliente", description = "API Cliente")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody ClienteDTO clienteDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(clienteDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody ClienteDTO cliente){
        return ResponseEntity.ok().body(service.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
