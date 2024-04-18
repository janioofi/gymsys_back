package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PagamentoDTO;
import br.janioofi.msgym.domain.entities.Pagamento;
import br.janioofi.msgym.domain.services.PagamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pagamento", description = "API Pagamento")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    @GetMapping
    public ResponseEntity<List<Pagamento>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pagamento> create(@RequestBody PagamentoDTO pagamento){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(pagamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pagamento> update(@PathVariable Long id, @RequestBody PagamentoDTO pagamentoDTO){
        return ResponseEntity.ok().body(service.update(id, pagamentoDTO));
    }
}
