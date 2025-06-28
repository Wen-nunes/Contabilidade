package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.Venda;
import com.example.apifinanceiro.model.Fornecedor;
import com.example.apifinanceiro.model.Patrimonio;
import com.example.apifinanceiro.model.Mercadoria;
import com.example.apifinanceiro.model.Cliente;
import com.example.apifinanceiro.dto.VendaDTO;
import com.example.apifinanceiro.dto.VendaCreateDTO;
import com.example.apifinanceiro.repository.VendaRepository;
import com.example.apifinanceiro.repository.FornecedorRepository;
import com.example.apifinanceiro.repository.PatrimonioRepository;
import com.example.apifinanceiro.repository.MercadoriaRepository;
import com.example.apifinanceiro.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private PatrimonioRepository patrimonioRepository;
    
    @Autowired
    private MercadoriaRepository mercadoriaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    private static final int MAX_PARCELAS = 10;
    private static final double TAXA_ICMS = 0.18; // 18% de ICMS

    @GetMapping
    public List<Venda> listarVendas() {
        return vendaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVenda(@PathVariable int id) {
        return vendaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/teste")
    public ResponseEntity<String> testeVendas() {
        List<Venda> todasVendas = vendaRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Total de vendas no banco: ").append(todasVendas.size()).append("\n");
        
        for (Venda venda : todasVendas) {
            sb.append("Venda ID: ").append(venda.getId())
              .append(", Mercadoria: ").append(venda.getNomeMercadoria())
              .append(", Fornecedor: ").append(venda.getFornecedor() != null ? venda.getFornecedor().getId() : "null")
              .append(", Valor: ").append(venda.getValor_total())
              .append("\n");
        }
        
        return ResponseEntity.ok(sb.toString());
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public List<Venda> buscarVendasPorFornecedor(@PathVariable int fornecedorId) {
        return vendaRepository.findByFornecedorId(fornecedorId);
    }

    @GetMapping("/fornecedor/{fornecedorId}/dto")
    public List<VendaDTO> buscarVendasPorFornecedorDTO(@PathVariable int fornecedorId) {
        List<Venda> vendas = vendaRepository.findByFornecedorId(fornecedorId);
        return vendas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/cliente/{clienteId}/dto")
    public List<VendaDTO> buscarVendasPorClienteDTO(@PathVariable int clienteId) {
        List<Venda> vendas = vendaRepository.findByClienteId((clienteId));
        return vendas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private VendaDTO convertToDTO(Venda venda) {
        String fornecedorNome = null;
        String clienteNome = null;
        
        if (venda.getFornecedor() != null) {
            fornecedorNome = venda.getFornecedor().getNome();
        }
        
        if (venda.getCliente() != null) {
            clienteNome = venda.getCliente().getNome();
        }
        
        return new VendaDTO(
            venda.getId(),
            venda.isCredito(),
            venda.isDebito(),
            venda.getValor_total(),
            venda.getIcms(),
            venda.getNumero_parcelas(),
            venda.getValor_parcela(),
            venda.getDataVenda(),
            venda.getNomeMercadoria(),
            venda.getQuantidade(),
            venda.getPrecoUnitario(),
            venda.getFornecedor() != null ? venda.getFornecedor().getId() : null,
            fornecedorNome,
            venda.getCliente() != null ? venda.getCliente().getId() : null,
            clienteNome
        );
    }

    @PostMapping
    public ResponseEntity<Venda> criarVenda(@RequestBody VendaCreateDTO vendaDTO) {
        try {
            System.out.println("Iniciando criação de venda: " + vendaDTO.getNomeMercadoria());
            System.out.println("Dados recebidos: " + vendaDTO);
            
            // Validação do número de parcelas
            if (vendaDTO.isCredito() && vendaDTO.getNumeroParcelas() > MAX_PARCELAS) {
                System.err.println("Número de parcelas excede o máximo permitido");
                return ResponseEntity.badRequest().build();
            }

            // Validação de forma de pagamento
            if (vendaDTO.isDebito() && vendaDTO.isCredito()) {
                System.err.println("Forma de pagamento inválida: não pode ser débito e crédito");
                return ResponseEntity.badRequest().build();
            }
            
            if (!vendaDTO.isDebito() && !vendaDTO.isCredito()) {
                System.err.println("Forma de pagamento não especificada");
                return ResponseEntity.badRequest().build();
            }

            // Verificar se fornecedor e cliente existem
            Fornecedor fornecedor = fornecedorRepository.findById(vendaDTO.getFornecedorId())
                    .orElse(null);
            if (fornecedor == null) {
                System.err.println("Fornecedor não encontrado com ID: " + vendaDTO.getFornecedorId());
                return ResponseEntity.badRequest().build();
            }
            
            Cliente cliente = clienteRepository.findById(vendaDTO.getClienteId())
                    .orElse(null);
            if (cliente == null) {
                System.err.println("Cliente não encontrado com ID: " + vendaDTO.getClienteId());
                return ResponseEntity.badRequest().build();
            }

            // Verificar estoque antes de vender
            Mercadoria mercadoria = mercadoriaRepository.findByNome(vendaDTO.getNomeMercadoria());
            if (mercadoria == null) {
                System.err.println("Mercadoria não encontrada: " + vendaDTO.getNomeMercadoria());
                return ResponseEntity.badRequest().build();
            }
            
            if (mercadoria.getQuantidade() < vendaDTO.getQuantidade()) {
                System.err.println("Estoque insuficiente. Disponível: " + mercadoria.getQuantidade() + ", Solicitado: " + vendaDTO.getQuantidade());
                return ResponseEntity.badRequest().build();
            }

            // Criar objeto Venda a partir do DTO
            Venda venda = new Venda();
            venda.setNomeMercadoria(vendaDTO.getNomeMercadoria());
            venda.setQuantidade(vendaDTO.getQuantidade());
            venda.setPrecoUnitario(vendaDTO.getPrecoUnitario());
            venda.setDebito(vendaDTO.isDebito());
            venda.setCredito(vendaDTO.isCredito());
            venda.setNumero_parcelas(vendaDTO.getNumeroParcelas());
            venda.setFornecedor(fornecedor);
            venda.setCliente(cliente);

            // Calcula o valor total da venda
            double valorTotal = vendaDTO.getPrecoUnitario() * vendaDTO.getQuantidade();
            venda.setValor_total(valorTotal);

            // Calcula o ICMS sobre o valor total
            double icms = valorTotal * TAXA_ICMS;
            venda.setIcms(icms);

            // Configura o pagamento
            if (vendaDTO.isDebito()) {
                venda.setCredito(false);
                venda.setNumero_parcelas(0);
                venda.setValor_parcela(0);
            } else if (vendaDTO.isCredito()) {
                venda.setDebito(false);
                if (vendaDTO.getNumeroParcelas() <= 0) {
                    System.err.println("Número de parcelas deve ser maior que zero para pagamento a crédito");
                    return ResponseEntity.badRequest().build();
                }
                double valorParcela = (valorTotal + icms) / vendaDTO.getNumeroParcelas();
                venda.setValor_parcela(valorParcela);
            }

            // Atualiza o patrimônio do fornecedor
            Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(fornecedor.getId());
            if (patrimonio != null) {
                if (vendaDTO.isDebito()) {
                    // Adiciona ao capital social se for à vista
                    double capitalAnterior = patrimonio.getCapitalSocial();
                    patrimonio.setCapitalSocial(patrimonio.getCapitalSocial() + valorTotal + icms);
                    patrimonioRepository.save(patrimonio);
                    System.out.println("Patrimônio atualizado (à vista): " + capitalAnterior + " -> " + patrimonio.getCapitalSocial());
                } else {
                    System.out.println("Venda a prazo - patrimônio não será atualizado imediatamente");
                }
            } else {
                System.err.println("Patrimônio não encontrado para fornecedor ID: " + fornecedor.getId());
            }

            // Atualizar estoque - diminuir quantidade vendida
            int estoqueAnterior = mercadoria.getQuantidade();
            mercadoria.setQuantidade(mercadoria.getQuantidade() - vendaDTO.getQuantidade());
            mercadoriaRepository.save(mercadoria);
            System.out.println("Estoque atualizado: " + estoqueAnterior + " -> " + mercadoria.getQuantidade());
            System.out.println("Mercadoria: " + mercadoria.getNome() + " | Quantidade vendida: " + vendaDTO.getQuantidade());

            Venda vendaSalva = vendaRepository.save(venda);
            System.out.println("Venda salva com sucesso. ID: " + vendaSalva.getId());
            
            return ResponseEntity.ok(vendaSalva);
        } catch (Exception e) {
            System.err.println("Erro ao criar venda: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable int id) {
        return vendaRepository.findById(id)
                .map(venda -> {
                    vendaRepository.delete(venda);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 