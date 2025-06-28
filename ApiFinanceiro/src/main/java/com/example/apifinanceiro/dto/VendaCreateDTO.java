package com.example.apifinanceiro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class VendaCreateDTO {
    private Integer fornecedorId;
    private Integer clienteId;
    private String nomeMercadoria;
    private int quantidade;
    private double precoUnitario;
    private boolean debito;
    private boolean credito;
    private int numeroParcelas;
    
    public VendaCreateDTO() {}
    
    public VendaCreateDTO(Integer fornecedorId, Integer clienteId, String nomeMercadoria, 
                         int quantidade, double precoUnitario, boolean debito, 
                         boolean credito, int numeroParcelas) {
        this.fornecedorId = fornecedorId;
        this.clienteId = clienteId;
        this.nomeMercadoria = nomeMercadoria;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.debito = debito;
        this.credito = credito;
        this.numeroParcelas = numeroParcelas;
    }
    
    // Getters e Setters
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    
    public String getNomeMercadoria() { return nomeMercadoria; }
    public void setNomeMercadoria(String nomeMercadoria) { this.nomeMercadoria = nomeMercadoria; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    
    public boolean isDebito() { return debito; }
    public void setDebito(boolean debito) { this.debito = debito; }
    
    public boolean isCredito() { return credito; }
    public void setCredito(boolean credito) { this.credito = credito; }
    
    public int getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(int numeroParcelas) { this.numeroParcelas = numeroParcelas; }
} 