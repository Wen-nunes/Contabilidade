package com.example.apifinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false)
    private double valor;
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCompra;
    
    @Column(nullable = false)
    private boolean pagamentoVista;
    
    @Column(nullable = false)
    private boolean pagamentoPrazo;
    
    private int numeroParcelas;
    
    private double valorParcela;
    
    @ManyToOne
    @JoinColumn(name = "fornecedor_bem_id")
    @JsonIgnore
    private Fornecedor fornecedor;

    public Bem() {
        this.dataCompra = LocalDateTime.now();
        this.pagamentoVista = true;
        this.pagamentoPrazo = false;
    }
    
    public Bem(String nome, String descricao, double valor, Fornecedor fornecedor) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.fornecedor = fornecedor;
    }
    
    public Bem(String nome, String descricao, double valor, Fornecedor fornecedor, 
               boolean pagamentoVista, boolean pagamentoPrazo, int numeroParcelas) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.fornecedor = fornecedor;
        this.pagamentoVista = pagamentoVista;
        this.pagamentoPrazo = pagamentoPrazo;
        this.numeroParcelas = numeroParcelas;
        
        if (pagamentoPrazo && numeroParcelas > 0) {
            this.valorParcela = this.valor / numeroParcelas;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public LocalDateTime getDataCompra() {
        return dataCompra;
    }
    
    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }
    
    public boolean isPagamentoVista() {
        return pagamentoVista;
    }
    
    public void setPagamentoVista(boolean pagamentoVista) {
        this.pagamentoVista = pagamentoVista;
    }
    
    public boolean isPagamentoPrazo() {
        return pagamentoPrazo;
    }
    
    public void setPagamentoPrazo(boolean pagamentoPrazo) {
        this.pagamentoPrazo = pagamentoPrazo;
    }
    
    public int getNumeroParcelas() {
        return numeroParcelas;
    }
    
    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
        if (pagamentoPrazo && numeroParcelas > 0) {
            this.valorParcela = this.valor / numeroParcelas;
        }
    }
    
    public double getValorParcela() {
        return valorParcela;
    }
    
    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bem bem = (Bem) o;
        return id == bem.id && Double.compare(bem.valor, valor) == 0 && pagamentoVista == bem.pagamentoVista && pagamentoPrazo == bem.pagamentoPrazo && numeroParcelas == bem.numeroParcelas && Double.compare(bem.valorParcela, valorParcela) == 0 && Objects.equals(nome, bem.nome) && Objects.equals(descricao, bem.descricao) && Objects.equals(dataCompra, bem.dataCompra) && Objects.equals(fornecedor, bem.fornecedor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, valor, dataCompra, pagamentoVista, pagamentoPrazo, numeroParcelas, valorParcela, fornecedor);
    }
    
    @Override
    public String toString() {
        return "Bem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", dataCompra=" + dataCompra +
                ", pagamentoVista=" + pagamentoVista +
                ", pagamentoPrazo=" + pagamentoPrazo +
                ", numeroParcelas=" + numeroParcelas +
                ", valorParcela=" + valorParcela +
                ", fornecedor=" + fornecedor +
                '}';
    }
} 