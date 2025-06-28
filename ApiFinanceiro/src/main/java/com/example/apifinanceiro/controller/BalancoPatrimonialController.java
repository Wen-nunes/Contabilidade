package com.example.apifinanceiro.controller;

import com.example.apifinanceiro.model.Patrimonio;
import com.example.apifinanceiro.model.Venda;
import com.example.apifinanceiro.model.CompraMercadoria;
import com.example.apifinanceiro.model.Bem;
import com.example.apifinanceiro.repository.PatrimonioRepository;
import com.example.apifinanceiro.repository.VendaRepository;
import com.example.apifinanceiro.repository.CompraMercadoriaRepository;
import com.example.apifinanceiro.repository.BemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/balanco")
public class BalancoPatrimonialController {

    @Autowired
    private PatrimonioRepository patrimonioRepository;
    
    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private CompraMercadoriaRepository compraMercadoriaRepository;
    
    @Autowired
    private BemRepository bemRepository;

    @GetMapping("/{fornecedorId}")
    public ResponseEntity<Map<String, Object>> gerarBalancoPatrimonial(@PathVariable int fornecedorId) {
        try {
            System.out.println("Gerando balanço para fornecedor ID: " + fornecedorId);
            
            Map<String, Object> balanco = new HashMap<>();
            
            // Busca o patrimônio do fornecedor
            Patrimonio patrimonio = patrimonioRepository.findByFornecedorId(fornecedorId);
            if (patrimonio == null) {
                System.out.println("Patrimônio não encontrado para fornecedor ID: " + fornecedorId);
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("Patrimônio encontrado: " + patrimonio.getCapitalSocial());
            
            // Busca todas as vendas do fornecedor
            List<Venda> vendas = vendaRepository.findByFornecedorId(fornecedorId);
            
            System.out.println("Vendas encontradas: " + vendas.size());
            
            // Busca todas as compras do fornecedor
            List<CompraMercadoria> compras = compraMercadoriaRepository.findByFornecedorId(fornecedorId);
            
            System.out.println("Compras encontradas: " + compras.size());
            
            // Busca todos os bens do fornecedor
            List<Bem> bens = bemRepository.findByFornecedorId(fornecedorId);
            
            System.out.println("Bens encontrados: " + bens.size());
            
            // Calcula valores a receber (vendas a prazo)
            double valoresAReceber = vendas.stream()
                    .filter(Venda::isCredito)
                    .mapToDouble(v -> v.getValor_total() + v.getIcms())
                    .sum();
            
            // Calcula valores a pagar (compras a prazo + bens a prazo)
            double valoresAPagarCompras = compras.stream()
                    .filter(CompraMercadoria::isPagamentoPrazo)
                    .mapToDouble(CompraMercadoria::getValorTotal)
                    .sum();
            
            double valoresAPagarBens = bens.stream()
                    .filter(Bem::isPagamentoPrazo)
                    .mapToDouble(Bem::getValor)
                    .sum();
            
            double valoresAPagar = valoresAPagarCompras + valoresAPagarBens;
            
            // Calcula total de vendas à vista
            double vendasAVista = vendas.stream()
                    .filter(Venda::isDebito)
                    .mapToDouble(v -> v.getValor_total() + v.getIcms())
                    .sum();
            
            // Calcula total de compras à vista (mercadorias + bens)
            double comprasAVistaMercadorias = compras.stream()
                    .filter(CompraMercadoria::isPagamentoVista)
                    .mapToDouble(CompraMercadoria::getValorTotal)
                    .sum();
            
            double comprasAVistaBens = bens.stream()
                    .filter(Bem::isPagamentoVista)
                    .mapToDouble(Bem::getValor)
                    .sum();
            
            double comprasAVista = comprasAVistaMercadorias + comprasAVistaBens;
            
            // Calcula valor total dos bens (apenas para exibição)
            double valorTotalBens = bens.stream()
                    .mapToDouble(Bem::getValor)
                    .sum();
            
            System.out.println("Valores calculados - A receber: " + valoresAReceber + 
                             ", A pagar: " + valoresAPagar + 
                             " (compras: " + valoresAPagarCompras + ", bens: " + valoresAPagarBens + ")" +
                             ", Bens: " + valorTotalBens);
            
            // Monta o balanço patrimonial
            balanco.put("fornecedorId", fornecedorId);
            balanco.put("capitalSocial", patrimonio.getCapitalSocial());
            balanco.put("valorTotalBens", valorTotalBens);
            balanco.put("bens", bens);
            balanco.put("valoresAReceber", valoresAReceber);
            balanco.put("valoresAPagar", valoresAPagar);
            balanco.put("valoresAPagarCompras", valoresAPagarCompras);
            balanco.put("valoresAPagarBens", valoresAPagarBens);
            balanco.put("vendasAVista", vendasAVista);
            balanco.put("comprasAVista", comprasAVista);
            balanco.put("comprasAVistaMercadorias", comprasAVistaMercadorias);
            balanco.put("comprasAVistaBens", comprasAVistaBens);
            
            // Calcula o patrimônio líquido
            double patrimonioLiquido = patrimonio.getCapitalSocial() + valoresAReceber - valoresAPagar + vendasAVista - comprasAVista + valorTotalBens;
            balanco.put("patrimonioLiquido", patrimonioLiquido);
            
            // Detalhes das vendas
            balanco.put("totalVendas", vendas.size());
            balanco.put("vendasCredito", vendas.stream().filter(Venda::isCredito).count());
            balanco.put("vendasDebito", vendas.stream().filter(Venda::isDebito).count());
            
            // Detalhes das compras
            balanco.put("totalCompras", compras.size());
            balanco.put("comprasPrazo", compras.stream().filter(CompraMercadoria::isPagamentoPrazo).count());
            balanco.put("comprasVista", compras.stream().filter(CompraMercadoria::isPagamentoVista).count());
            
            // Detalhes dos bens
            balanco.put("totalBens", bens.size());
            balanco.put("bensPrazo", bens.stream().filter(Bem::isPagamentoPrazo).count());
            balanco.put("bensVista", bens.stream().filter(Bem::isPagamentoVista).count());
            
            System.out.println("Balanço gerado com sucesso");
            return ResponseEntity.ok(balanco);
        } catch (Exception e) {
            System.err.println("Erro ao gerar balanço: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumoGeral() {
        try {
            Map<String, Object> resumo = new HashMap<>();
            
            List<Patrimonio> patrimonios = patrimonioRepository.findAll();
            List<Venda> vendas = vendaRepository.findAll();
            List<CompraMercadoria> compras = compraMercadoriaRepository.findAll();
            List<Bem> bens = bemRepository.findAll();
            
            double totalCapitalSocial = patrimonios.stream()
                    .mapToDouble(Patrimonio::getCapitalSocial)
                    .sum();
            
            double totalVendasAVista = vendas.stream()
                    .filter(Venda::isDebito)
                    .mapToDouble(v -> v.getValor_total() + v.getIcms())
                    .sum();
            
            double totalVendasPrazo = vendas.stream()
                    .filter(Venda::isCredito)
                    .mapToDouble(v -> v.getValor_total() + v.getIcms())
                    .sum();
            
            double totalComprasAVista = compras.stream()
                    .filter(CompraMercadoria::isPagamentoVista)
                    .mapToDouble(CompraMercadoria::getValorTotal)
                    .sum();
            
            double totalComprasPrazo = compras.stream()
                    .filter(CompraMercadoria::isPagamentoPrazo)
                    .mapToDouble(CompraMercadoria::getValorTotal)
                    .sum();
            
            double totalBensAVista = bens.stream()
                    .filter(Bem::isPagamentoVista)
                    .mapToDouble(Bem::getValor)
                    .sum();
            
            double totalBensPrazo = bens.stream()
                    .filter(Bem::isPagamentoPrazo)
                    .mapToDouble(Bem::getValor)
                    .sum();
            
            double totalBens = bens.stream()
                    .mapToDouble(Bem::getValor)
                    .sum();
            
            resumo.put("totalFornecedores", patrimonios.size());
            resumo.put("totalCapitalSocial", totalCapitalSocial);
            resumo.put("totalBens", totalBens);
            resumo.put("totalBensAVista", totalBensAVista);
            resumo.put("totalBensPrazo", totalBensPrazo);
            resumo.put("totalVendasAVista", totalVendasAVista);
            resumo.put("totalVendasPrazo", totalVendasPrazo);
            resumo.put("totalComprasAVista", totalComprasAVista);
            resumo.put("totalComprasPrazo", totalComprasPrazo);
            resumo.put("totalVendas", vendas.size());
            resumo.put("totalCompras", compras.size());
            resumo.put("totalBensCount", bens.size());
            
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            System.err.println("Erro ao gerar resumo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
} 