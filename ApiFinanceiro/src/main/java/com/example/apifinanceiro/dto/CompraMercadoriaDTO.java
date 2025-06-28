package com.example.apifinanceiro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CompraMercadoriaDTO {
    private Integer fornecedorId;
    private String nomeMercadoria;
    private String descricao;
    private double precoCompra;
    private double precoVenda;
    private int quantidade;
    private boolean pagamentoVista;
    private boolean pagamentoPrazo;
    private int numeroParcelas;
    
    public CompraMercadoriaDTO() {}
    
    public CompraMercadoriaDTO(Integer fornecedorId, String nomeMercadoria, String descricao, 
                              double precoCompra, double precoVenda, int quantidade,
                              boolean pagamentoVista, boolean pagamentoPrazo, int numeroParcelas) {
        this.fornecedorId = fornecedorId;
        this.nomeMercadoria = nomeMercadoria;
        this.descricao = descricao;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidade = quantidade;
        this.pagamentoVista = pagamentoVista;
        this.pagamentoPrazo = pagamentoPrazo;
        this.numeroParcelas = numeroParcelas;
    }
    
    // Getters e Setters
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    
    public String getNomeMercadoria() { return nomeMercadoria; }
    public void setNomeMercadoria(String nomeMercadoria) { this.nomeMercadoria = nomeMercadoria; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public double getPrecoCompra() { return precoCompra; }
    public void setPrecoCompra(double precoCompra) { this.precoCompra = precoCompra; }
    
    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public boolean isPagamentoVista() { return pagamentoVista; }
    public void setPagamentoVista(boolean pagamentoVista) { this.pagamentoVista = pagamentoVista; }
    
    public boolean isPagamentoPrazo() { return pagamentoPrazo; }
    public void setPagamentoPrazo(boolean pagamentoPrazo) { this.pagamentoPrazo = pagamentoPrazo; }
    
    public int getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(int numeroParcelas) { this.numeroParcelas = numeroParcelas; }
} 