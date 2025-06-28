package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.Patrimonio;
import com.example.apifinanceiro.repository.PatrimonioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patrimonio")
public class PatrimonioController {

    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @GetMapping
    public List<Patrimonio> listarPatrimonios() {
        return patrimonioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patrimonio> buscarPatrimonio(@PathVariable int id) {
        return patrimonioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Patrimonio> buscarPatrimonioPorFornecedor(@PathVariable int fornecedorId) {
        Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(fornecedorId);
        if (patrimonio != null) {
            return ResponseEntity.ok(patrimonio);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Patrimonio> criarPatrimonio(@RequestBody Patrimonio patrimonio) {
        return ResponseEntity.ok(patrimonioRepository.save(patrimonio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patrimonio> atualizarPatrimonio(@PathVariable int id, @RequestBody Patrimonio patrimonioAtualizado) {
        return patrimonioRepository.findById(id)
                .map(patrimonio -> {
                    patrimonio.setCapitalSocial(patrimonioAtualizado.getCapitalSocial());
                    return ResponseEntity.ok(patrimonioRepository.save(patrimonio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPatrimonio(@PathVariable int id) {
        return patrimonioRepository.findById(id)
                .map(patrimonio -> {
                    patrimonioRepository.delete(patrimonio);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 