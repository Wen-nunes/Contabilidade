package com.example.apifinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CompraMercadoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    
    @Column(nullable = false)
    private String nomeMercadoria;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false)
    private double precoCompra;
    
    @Column(nullable = false)
    private double precoVenda;
    
    @Column(nullable = false)
    private int quantidade;
    
    @Column(nullable = false)
    private double valorTotal;
    
    @Column(nullable = false)
    private boolean pagamentoVista;
    
    @Column(nullable = false)
    private boolean pagamentoPrazo;
    
    private int numeroParcelas;
    
    private double valorParcela;
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCompra;
    
    @ManyToOne
    @JoinColumn(name = "fornecedor_compra_id")
    @JsonIgnore
    private Fornecedor fornecedor;
    
    public CompraMercadoria() {
        this.dataCompra = LocalDateTime.now();
    }
    
    public CompraMercadoria(String nomeMercadoria, String descricao, double precoCompra, 
                           double precoVenda, int quantidade, boolean pagamentoVista, 
                           boolean pagamentoPrazo, int numeroParcelas, Fornecedor fornecedor) {
        this();
        this.nomeMercadoria = nomeMercadoria;
        this.descricao = descricao;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidade = quantidade;
        this.pagamentoVista = pagamentoVista;
        this.pagamentoPrazo = pagamentoPrazo;
        this.numeroParcelas = numeroParcelas;
        this.fornecedor = fornecedor;
        this.valorTotal = precoCompra * quantidade;
        
        if (pagamentoPrazo && numeroParcelas > 0) {
            this.valorParcela = this.valorTotal / numeroParcelas;
        }
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNomeMercadoria() {
        return nomeMercadoria;
    }
    
    public void setNomeMercadoria(String nomeMercadoria) {
        this.nomeMercadoria = nomeMercadoria;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public double getPrecoCompra() {
        return precoCompra;
    }
    
    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }
    
    public double getPrecoVenda() {
        return precoVenda;
    }
    
    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public double getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
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
    }
    
    public double getValorParcela() {
        return valorParcela;
    }
    
    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }
    
    public LocalDateTime getDataCompra() {
        return dataCompra;
    }
    
    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
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
        CompraMercadoria that = (CompraMercadoria) o;
        return id == that.id && Double.compare(that.precoCompra, precoCompra) == 0 && Double.compare(that.precoVenda, precoVenda) == 0 && quantidade == that.quantidade && Double.compare(that.valorTotal, valorTotal) == 0 && pagamentoVista == that.pagamentoVista && pagamentoPrazo == that.pagamentoPrazo && numeroParcelas == that.numeroParcelas && Double.compare(that.valorParcela, valorParcela) == 0 && Objects.equals(nomeMercadoria, that.nomeMercadoria) && Objects.equals(descricao, that.descricao) && Objects.equals(dataCompra, that.dataCompra) && Objects.equals(fornecedor, that.fornecedor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nomeMercadoria, descricao, precoCompra, precoVenda, quantidade, valorTotal, pagamentoVista, pagamentoPrazo, numeroParcelas, valorParcela, dataCompra, fornecedor);
    }
    
    @Override
    public String toString() {
        return "CompraMercadoria{" +
                "id=" + id +
                ", nomeMercadoria='" + nomeMercadoria + '\'' +
                ", descricao='" + descricao + '\'' +
                ", precoCompra=" + precoCompra +
                ", precoVenda=" + precoVenda +
                ", quantidade=" + quantidade +
                ", valorTotal=" + valorTotal +
                ", pagamentoVista=" + pagamentoVista +
                ", pagamentoPrazo=" + pagamentoPrazo +
                ", numeroParcelas=" + numeroParcelas +
                ", valorParcela=" + valorParcela +
                ", dataCompra=" + dataCompra +
                ", fornecedor=" + fornecedor +
                '}';
    }
} 