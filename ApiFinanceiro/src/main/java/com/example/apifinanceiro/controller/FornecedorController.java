package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.Fornecedor;
import com.example.apifinanceiro.model.Patrimonio;
import com.example.apifinanceiro.repository.FornecedorRepository;
import com.example.apifinanceiro.repository.PatrimonioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @GetMapping
    public List<Fornecedor> listarFornecedores() {
        return fornecedorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarFornecedor(@PathVariable int id) {
        return fornecedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fornecedor> criarFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            // Salva o fornecedor
            Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
            
            // Cria automaticamente o patrimônio para o fornecedor
            Patrimonio patrimonio = new Patrimonio();
            patrimonio.setCapitalSocial(fornecedor.getCapitalSocial());
            patrimonio.setFornecedor(fornecedorSalvo);
            patrimonioRepository.save(patrimonio);
            
            return ResponseEntity.ok(fornecedorSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizarFornecedor(@PathVariable int id, @RequestBody Fornecedor fornecedorAtualizado) {
        return fornecedorRepository.findById(id)
                .map(fornecedor -> {
                    fornecedor.setNome(fornecedorAtualizado.getNome());
                    fornecedor.setEndereco(fornecedorAtualizado.getEndereco());
                    fornecedor.setCapitalSocial(fornecedorAtualizado.getCapitalSocial());
                    
                    // Atualiza o patrimônio se o capital social mudou
                    Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(fornecedor.getId());
                    if (patrimonio != null) {
                        patrimonio.setCapitalSocial(fornecedorAtualizado.getCapitalSocial());
                        patrimonioRepository.save(patrimonio);
                    }
                    
                    return ResponseEntity.ok(fornecedorRepository.save(fornecedor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable int id) {
        return fornecedorRepository.findById(id)
                .map(fornecedor -> {
                    fornecedorRepository.delete(fornecedor);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 