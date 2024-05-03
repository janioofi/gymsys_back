package br.janioofi.msgym.controllers;

import br.janioofi.msgym.domain.dtos.PagamentoDTO;
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
    public ResponseEntity<List<PagamentoDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> create(@RequestBody PagamentoDTO pagamento){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(pagamento));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<PagamentoDTO>> pagamentosPorPeriodo(@RequestParam String data_inicio, @RequestParam String data_final){
        return ResponseEntity.ok().body(service.pagamentosPoPeriodo(data_inicio, data_final));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> update(@PathVariable Long id, @RequestBody PagamentoDTO pagamentoDTO){
        return ResponseEntity.ok().body(service.update(id, pagamentoDTO));
    }
}
