package com.example.apifinanceiro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class BemDTO {
    private Integer fornecedorId;
    private String nome;
    private String descricao;
    private double valor;
    private boolean pagamentoVista;
    private boolean pagamentoPrazo;
    private int numeroParcelas;
    
    public BemDTO() {}
    
    public BemDTO(Integer fornecedorId, String nome, String descricao, double valor, 
                  boolean pagamentoVista, boolean pagamentoPrazo, int numeroParcelas) {
        this.fornecedorId = fornecedorId;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.pagamentoVista = pagamentoVista;
        this.pagamentoPrazo = pagamentoPrazo;
        this.numeroParcelas = numeroParcelas;
    }
    
    // Getters e Setters
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public boolean isPagamentoVista() { return pagamentoVista; }
    public void setPagamentoVista(boolean pagamentoVista) { this.pagamentoVista = pagamentoVista; }
    
    public boolean isPagamentoPrazo() { return pagamentoPrazo; }
    public void setPagamentoPrazo(boolean pagamentoPrazo) { this.pagamentoPrazo = pagamentoPrazo; }
    
    public int getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(int numeroParcelas) { this.numeroParcelas = numeroParcelas; }
} 