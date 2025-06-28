package com.example.apifinanceiro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class VendaDTO {
    private int id;
    private boolean credito;
    private boolean debito;
    private double valorTotal;
    private double icms;
    private int numeroParcelas;
    private double valorParcela;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataVenda;
    
    private String nomeMercadoria;
    private int quantidade;
    private double precoUnitario;
    
    // IDs apenas, sem objetos completos
    private Integer fornecedorId;
    private String fornecedorNome;
    private Integer clienteId;
    private String clienteNome;
    
    public VendaDTO() {}
    
    public VendaDTO(int id, boolean credito, boolean debito, double valorTotal, 
                   double icms, int numeroParcelas, double valorParcela, 
                   LocalDateTime dataVenda, String nomeMercadoria, int quantidade, 
                   double precoUnitario, Integer fornecedorId, String fornecedorNome,
                   Integer clienteId, String clienteNome) {
        this.id = id;
        this.credito = credito;
        this.debito = debito;
        this.valorTotal = valorTotal;
        this.icms = icms;
        this.numeroParcelas = numeroParcelas;
        this.valorParcela = valorParcela;
        this.dataVenda = dataVenda;
        this.nomeMercadoria = nomeMercadoria;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.fornecedorId = fornecedorId;
        this.fornecedorNome = fornecedorNome;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public boolean isCredito() { return credito; }
    public void setCredito(boolean credito) { this.credito = credito; }
    
    public boolean isDebito() { return debito; }
    public void setDebito(boolean debito) { this.debito = debito; }
    
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    
    public double getIcms() { return icms; }
    public void setIcms(double icms) { this.icms = icms; }
    
    public int getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(int numeroParcelas) { this.numeroParcelas = numeroParcelas; }
    
    public double getValorParcela() { return valorParcela; }
    public void setValorParcela(double valorParcela) { this.valorParcela = valorParcela; }
    
    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
    
    public String getNomeMercadoria() { return nomeMercadoria; }
    public void setNomeMercadoria(String nomeMercadoria) { this.nomeMercadoria = nomeMercadoria; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    
    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    
    public String getFornecedorNome() { return fornecedorNome; }
    public void setFornecedorNome(String fornecedorNome) { this.fornecedorNome = fornecedorNome; }
    
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    
    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
} 