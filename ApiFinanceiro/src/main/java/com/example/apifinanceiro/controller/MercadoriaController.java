package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.Mercadoria;
import com.example.apifinanceiro.repository.MercadoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaController {

    @Autowired
    private MercadoriaRepository mercadoriaRepository;

    @GetMapping
    public List<Mercadoria> listarMercadorias() {
        return mercadoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> buscarMercadoria(@PathVariable int id) {
        return mercadoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estoque")
    public ResponseEntity<String> listarEstoque() {
        List<Mercadoria> mercadorias = mercadoriaRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTOQUE DE MERCADORIAS ===\n");
        sb.append("Total de produtos: ").append(mercadorias.size()).append("\n\n");
        
        for (Mercadoria mercadoria : mercadorias) {
            sb.append("ID: ").append(mercadoria.getId())
              .append(" | Nome: ").append(mercadoria.getNome())
              .append(" | Qtd: ").append(mercadoria.getQuantidade())
              .append(" | Preço Compra: R$ ").append(mercadoria.getPreco_de_compra())
              .append(" | Preço Venda: R$ ").append(mercadoria.getPreco_venda())
              .append("\n");
        }
        
        return ResponseEntity.ok(sb.toString());
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public List<Mercadoria> buscarMercadoriasPorFornecedor(@PathVariable int fornecedorId) {
        // Buscar todas as mercadorias que foram compradas pelo fornecedor
        return mercadoriaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Mercadoria> criarMercadoria(@RequestBody Mercadoria mercadoria) {
        try {
            Mercadoria mercadoriaSalva = mercadoriaRepository.save(mercadoria);
            return ResponseEntity.ok(mercadoriaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mercadoria> atualizarMercadoria(@PathVariable int id, @RequestBody Mercadoria mercadoriaAtualizada) {
        return mercadoriaRepository.findById(id)
                .map(mercadoria -> {
                    mercadoria.setNome(mercadoriaAtualizada.getNome());
                    mercadoria.setDescricao(mercadoriaAtualizada.getDescricao());
                    mercadoria.setPreco_de_compra(mercadoriaAtualizada.getPreco_de_compra());
                    mercadoria.setPreco_venda(mercadoriaAtualizada.getPreco_venda());
                    mercadoria.setQuantidade(mercadoriaAtualizada.getQuantidade());
                    return ResponseEntity.ok(mercadoriaRepository.save(mercadoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMercadoria(@PathVariable int id) {
        return mercadoriaRepository.findById(id)
                .map(mercadoria -> {
                    mercadoriaRepository.delete(mercadoria);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 