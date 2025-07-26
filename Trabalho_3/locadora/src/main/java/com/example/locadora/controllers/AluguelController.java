package com.example.locadora.controllers;

import com.example.locadora.models.Aluguel;
import com.example.locadora.services.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService service;

    @PostMapping("/alugar")
    public Aluguel realizarAluguel(
        @RequestParam Long clienteId,
        @RequestParam Long veiculoId,
        @RequestParam String dataFim
    ) {
        return service.realizarAluguel(clienteId, veiculoId, LocalDate.parse(dataFim));
    }

    @PostMapping("/finalizar/{id}")
    public Aluguel finalizarAluguel(@PathVariable Long id) {
        return service.finalizarAluguel(id);
    }

    @GetMapping
    public List<Aluguel> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluguel> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.finalizarAluguel(id);
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
