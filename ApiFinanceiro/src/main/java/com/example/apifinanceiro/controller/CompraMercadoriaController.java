package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.CompraMercadoria;
import com.example.apifinanceiro.model.Fornecedor;
import com.example.apifinanceiro.model.Patrimonio;
import com.example.apifinanceiro.model.Mercadoria;
import com.example.apifinanceiro.dto.CompraMercadoriaDTO;
import com.example.apifinanceiro.repository.CompraMercadoriaRepository;
import com.example.apifinanceiro.repository.FornecedorRepository;
import com.example.apifinanceiro.repository.PatrimonioRepository;
import com.example.apifinanceiro.repository.MercadoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compras")
public class CompraMercadoriaController {

    @Autowired
    private CompraMercadoriaRepository compraMercadoriaRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private PatrimonioRepository patrimonioRepository;
    
    @Autowired
    private MercadoriaRepository mercadoriaRepository;

    private static final int MAX_PARCELAS = 12;

    @GetMapping
    public List<CompraMercadoria> listarCompras() {
        return compraMercadoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraMercadoria> buscarCompra(@PathVariable int id) {
        return compraMercadoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/fornecedor/{fornecedorId}")
    public List<CompraMercadoria> buscarComprasPorFornecedor(@PathVariable int fornecedorId) {
        return compraMercadoriaRepository.findByFornecedorId(fornecedorId);
    }
    
    @GetMapping("/fornecedor/{fornecedorId}/detalhado")
    public ResponseEntity<Map<String, Object>> buscarComprasDetalhadasPorFornecedor(@PathVariable int fornecedorId) {
        try {
            List<CompraMercadoria> compras = compraMercadoriaRepository.findByFornecedorId(fornecedorId);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("fornecedorId", fornecedorId);
            resultado.put("totalCompras", compras.size());
            
            // Compras à vista
            List<CompraMercadoria> comprasVista = compras.stream()
                .filter(CompraMercadoria::isPagamentoVista)
                .collect(java.util.stream.Collectors.toList());
            
            double totalComprasVista = comprasVista.stream()
                .mapToDouble(CompraMercadoria::getValorTotal)
                .sum();
            
            // Compras a prazo
            List<CompraMercadoria> comprasPrazo = compras.stream()
                .filter(CompraMercadoria::isPagamentoPrazo)
                .collect(java.util.stream.Collectors.toList());
            
            double totalComprasPrazo = comprasPrazo.stream()
                .mapToDouble(CompraMercadoria::getValorTotal)
                .sum();
            
            resultado.put("comprasVista", comprasVista);
            resultado.put("comprasPrazo", comprasPrazo);
            resultado.put("totalComprasVista", totalComprasVista);
            resultado.put("totalComprasPrazo", totalComprasPrazo);
            resultado.put("totalComprasVistaCount", comprasVista.size());
            resultado.put("totalComprasPrazoCount", comprasPrazo.size());
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<CompraMercadoria> criarCompra(@RequestBody CompraMercadoriaDTO compraDTO) {
        try {
            // Validação do número de parcelas
            if (compraDTO.isPagamentoPrazo() && (compraDTO.getNumeroParcelas() <= 0 || compraDTO.getNumeroParcelas() > MAX_PARCELAS)) {
                return ResponseEntity.badRequest().build();
            }

            // Validação de forma de pagamento
            if (compraDTO.isPagamentoVista() && compraDTO.isPagamentoPrazo()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (!compraDTO.isPagamentoVista() && !compraDTO.isPagamentoPrazo()) {
                return ResponseEntity.badRequest().build();
            }

            // Calcula o valor total da compra
            CompraMercadoria compra = new CompraMercadoria();
            compra.setNomeMercadoria(compraDTO.getNomeMercadoria());
            compra.setDescricao(compraDTO.getDescricao());
            compra.setPrecoCompra(compraDTO.getPrecoCompra());
            compra.setPrecoVenda(compraDTO.getPrecoVenda());
            compra.setQuantidade(compraDTO.getQuantidade());
            compra.setPagamentoVista(compraDTO.isPagamentoVista());
            compra.setPagamentoPrazo(compraDTO.isPagamentoPrazo());
            compra.setNumeroParcelas(compraDTO.getNumeroParcelas());
            compra.setValorTotal(compraDTO.getPrecoCompra() * compraDTO.getQuantidade());
            
            // Calcula o valor da parcela se for pagamento a prazo
            if (compraDTO.isPagamentoPrazo() && compraDTO.getNumeroParcelas() > 0) {
                compra.setValorParcela(compra.getValorTotal() / compraDTO.getNumeroParcelas());
            } else {
                compra.setValorParcela(0.0);
            }

            // Atualiza o capital social do fornecedor e patrimônio
            Fornecedor fornecedor = fornecedorRepository.findById(compraDTO.getFornecedorId())
                    .orElse(null);
            if (fornecedor != null) {
                if (compraDTO.isPagamentoVista()) {
                    // Desconta do capital social do fornecedor se for à vista
                    fornecedor.setCapitalSocial(fornecedor.getCapitalSocial() - compra.getValorTotal());
                    fornecedorRepository.save(fornecedor);
                    
                    // Atualiza também o patrimônio
                    Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(fornecedor.getId());
                    if (patrimonio != null) {
                        patrimonio.setCapitalSocial(fornecedor.getCapitalSocial());
                        patrimonioRepository.save(patrimonio);
                    }
                }
                
                // Associa o fornecedor à compra
                compra.setFornecedor(fornecedor);
            }

            // Gerenciar estoque - buscar ou criar mercadoria
            Mercadoria mercadoria = mercadoriaRepository.findByNome(compraDTO.getNomeMercadoria());
            if (mercadoria == null) {
                // Criar nova mercadoria se não existir
                mercadoria = new Mercadoria();
                mercadoria.setNome(compraDTO.getNomeMercadoria());
                mercadoria.setDescricao(compraDTO.getDescricao());
                mercadoria.setPreco_de_compra((float) compraDTO.getPrecoCompra());
                mercadoria.setPreco_venda((float) compraDTO.getPrecoVenda());
                mercadoria.setQuantidade(compraDTO.getQuantidade());
            } else {
                // Atualizar mercadoria existente
                mercadoria.setQuantidade(mercadoria.getQuantidade() + compraDTO.getQuantidade());
                mercadoria.setPreco_de_compra((float) compraDTO.getPrecoCompra());
                mercadoria.setPreco_venda((float) compraDTO.getPrecoVenda());
                if (compraDTO.getDescricao() != null && !compraDTO.getDescricao().isEmpty()) {
                    mercadoria.setDescricao(compraDTO.getDescricao());
                }
            }
            mercadoriaRepository.save(mercadoria);

            CompraMercadoria compraSalva = compraMercadoriaRepository.save(compra);
            return ResponseEntity.ok(compraSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraMercadoria> atualizarCompra(@PathVariable int id, @RequestBody CompraMercadoria compraAtualizada) {
        return compraMercadoriaRepository.findById(id)
                .map(compra -> {
                    compra.setNomeMercadoria(compraAtualizada.getNomeMercadoria());
                    compra.setDescricao(compraAtualizada.getDescricao());
                    compra.setPrecoCompra(compraAtualizada.getPrecoCompra());
                    compra.setPrecoVenda(compraAtualizada.getPrecoVenda());
                    compra.setQuantidade(compraAtualizada.getQuantidade());
                    compra.setPagamentoVista(compraAtualizada.isPagamentoVista());
                    compra.setPagamentoPrazo(compraAtualizada.isPagamentoPrazo());
                    compra.setNumeroParcelas(compraAtualizada.getNumeroParcelas());
                    compra.setValorTotal(compra.getPrecoCompra() * compra.getQuantidade());
                    
                    if (compra.isPagamentoPrazo() && compra.getNumeroParcelas() > 0) {
                        compra.setValorParcela(compra.getValorTotal() / compra.getNumeroParcelas());
                    } else {
                        compra.setValorParcela(0.0);
                    }
                    
                    return ResponseEntity.ok(compraMercadoriaRepository.save(compra));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCompra(@PathVariable int id) {
        return compraMercadoriaRepository.findById(id)
                .map(compra -> {
                    compraMercadoriaRepository.delete(compra);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 