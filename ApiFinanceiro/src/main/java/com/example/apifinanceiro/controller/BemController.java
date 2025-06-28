package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.Bem;
import com.example.apifinanceiro.model.Fornecedor;
import com.example.apifinanceiro.model.Patrimonio;
import com.example.apifinanceiro.dto.BemDTO;
import com.example.apifinanceiro.repository.BemRepository;
import com.example.apifinanceiro.repository.FornecedorRepository;
import com.example.apifinanceiro.repository.PatrimonioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bens")
public class BemController {

    @Autowired
    private BemRepository bemRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @GetMapping
    public List<Bem> listarBens() {
        return bemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bem> buscarBem(@PathVariable int id) {
        return bemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/fornecedor/{fornecedorId}")
    public List<Bem> buscarBensPorFornecedor(@PathVariable int fornecedorId) {
        return bemRepository.findByFornecedorId(fornecedorId);
    }

    @PostMapping
    public ResponseEntity<Bem> criarBem(@RequestBody BemDTO bemDTO) {
        try {
            System.out.println("Iniciando criação de bem: " + bemDTO.getNome());
            System.out.println("Dados recebidos: " + bemDTO);
            
            // Verifica se o fornecedor ID foi enviado
            if (bemDTO.getFornecedorId() == null) {
                System.err.println("Fornecedor ID não foi enviado no JSON");
                return ResponseEntity.badRequest().build();
            }
            
            // Verifica se o fornecedor existe
            Fornecedor fornecedor = fornecedorRepository.findById(bemDTO.getFornecedorId())
                    .orElse(null);
            if (fornecedor == null) {
                System.err.println("Fornecedor não encontrado com ID: " + bemDTO.getFornecedorId());
                return ResponseEntity.badRequest().build();
            }
            
            System.out.println("Fornecedor encontrado: " + fornecedor.getNome());
            
            // Cria o objeto Bem a partir do DTO
            Bem bem = new Bem();
            bem.setNome(bemDTO.getNome());
            bem.setDescricao(bemDTO.getDescricao());
            bem.setValor(bemDTO.getValor());
            bem.setPagamentoVista(bemDTO.isPagamentoVista());
            bem.setPagamentoPrazo(bemDTO.isPagamentoPrazo());
            bem.setNumeroParcelas(bemDTO.getNumeroParcelas());
            bem.setFornecedor(fornecedor);
            
            // Calcula o valor da parcela se for pagamento a prazo
            if (bem.isPagamentoPrazo() && bem.getNumeroParcelas() > 0) {
                bem.setValorParcela(bem.getValor() / bem.getNumeroParcelas());
            }
            
            // Atualiza o patrimônio do fornecedor apenas se for pagamento à vista
            if (bem.isPagamentoVista()) {
                Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(fornecedor.getId());
                if (patrimonio != null) {
                    double capitalAnterior = patrimonio.getCapitalSocial();
                    patrimonio.setCapitalSocial(patrimonio.getCapitalSocial() - bem.getValor());
                    patrimonioRepository.save(patrimonio);
                    System.out.println("Patrimônio atualizado (à vista): " + capitalAnterior + " -> " + patrimonio.getCapitalSocial());
                } else {
                    System.err.println("Patrimônio não encontrado para fornecedor ID: " + fornecedor.getId());
                }
            } else {
                System.out.println("Bem comprado a prazo - patrimônio não será descontado imediatamente");
            }
            
            Bem bemSalvo = bemRepository.save(bem);
            System.out.println("Bem salvo com sucesso. ID: " + bemSalvo.getId());
            
            // Tenta buscar o bem salvo para verificar se está correto
            Bem bemVerificado = bemRepository.findById(bemSalvo.getId()).orElse(null);
            if (bemVerificado != null) {
                System.out.println("Bem verificado: " + bemVerificado.getNome() + " - Fornecedor: " + bemVerificado.getFornecedor().getNome());
                System.out.println("Pagamento: " + (bemVerificado.isPagamentoVista() ? "À vista" : "A prazo (" + bemVerificado.getNumeroParcelas() + "x)"));
            }
            
            return ResponseEntity.ok(bemSalvo);
        } catch (Exception e) {
            System.err.println("Erro ao criar bem: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/teste")
    public ResponseEntity<Map<String, Object>> testeCriarBem(@RequestBody Bem bem) {
        try {
            System.out.println("Teste de criação de bem: " + bem.getNome());
            
            // Verifica se o fornecedor existe
            Fornecedor fornecedor = fornecedorRepository.findById(bem.getFornecedor().getId())
                    .orElse(null);
            if (fornecedor == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Garante que o bem está associado ao fornecedor
            bem.setFornecedor(fornecedor);
            
            // Calcula o valor da parcela se for pagamento a prazo
            if (bem.isPagamentoPrazo() && bem.getNumeroParcelas() > 0) {
                bem.setValorParcela(bem.getValor() / bem.getNumeroParcelas());
            }
            
            // Salva o bem
            Bem bemSalvo = bemRepository.save(bem);
            
            // Cria uma resposta simples sem relacionamentos complexos
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("id", bemSalvo.getId());
            resposta.put("nome", bemSalvo.getNome());
            resposta.put("descricao", bemSalvo.getDescricao());
            resposta.put("valor", bemSalvo.getValor());
            resposta.put("dataCompra", bemSalvo.getDataCompra());
            resposta.put("pagamentoVista", bemSalvo.isPagamentoVista());
            resposta.put("pagamentoPrazo", bemSalvo.isPagamentoPrazo());
            resposta.put("numeroParcelas", bemSalvo.getNumeroParcelas());
            resposta.put("valorParcela", bemSalvo.getValorParcela());
            resposta.put("fornecedorId", bemSalvo.getFornecedor().getId());
            resposta.put("fornecedorNome", bemSalvo.getFornecedor().getNome());
            
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro no teste: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bem> atualizarBem(@PathVariable int id, @RequestBody Bem bemAtualizado) {
        return (ResponseEntity<Bem>) bemRepository.findById(id)
                .map(bem -> {
                    try {
                        // Calcula a diferença de valor para ajustar o patrimônio
                        double diferencaValor = bemAtualizado.getValor() - bem.getValor();
                        
                        bem.setNome(bemAtualizado.getNome());
                        bem.setDescricao(bemAtualizado.getDescricao());
                        bem.setValor(bemAtualizado.getValor());
                        bem.setPagamentoVista(bemAtualizado.isPagamentoVista());
                        bem.setPagamentoPrazo(bemAtualizado.isPagamentoPrazo());
                        bem.setNumeroParcelas(bemAtualizado.getNumeroParcelas());
                        
                        // Calcula o valor da parcela se for pagamento a prazo
                        if (bem.isPagamentoPrazo() && bem.getNumeroParcelas() > 0) {
                            bem.setValorParcela(bem.getValor() / bem.getNumeroParcelas());
                        }
                        
                        // Atualiza o patrimônio se o valor mudou e for pagamento à vista
                        if (diferencaValor != 0 && bem.isPagamentoVista()) {
                            Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(bem.getFornecedor().getId());
                            if (patrimonio != null) {
                                patrimonio.setCapitalSocial(patrimonio.getCapitalSocial() - diferencaValor);
                                patrimonioRepository.save(patrimonio);
                            }
                        }
                        
                        return ResponseEntity.ok(bemRepository.save(bem));
                    } catch (Exception e) {
                        System.err.println("Erro ao atualizar bem: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.badRequest().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBem(@PathVariable int id) {
        return (ResponseEntity<Void>) bemRepository.findById(id)
                .map(bem -> {
                    try {
                        // Restaura o valor do bem ao patrimônio apenas se for pagamento à vista
                        if (bem.isPagamentoVista()) {
                            Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(bem.getFornecedor().getId());
                            if (patrimonio != null) {
                                patrimonio.setCapitalSocial(patrimonio.getCapitalSocial() + bem.getValor());
                                patrimonioRepository.save(patrimonio);
                            }
                        }
                        
                        bemRepository.delete(bem);
                        return ResponseEntity.ok().<Void>build();
                    } catch (Exception e) {
                        System.err.println("Erro ao deletar bem: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.badRequest().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 