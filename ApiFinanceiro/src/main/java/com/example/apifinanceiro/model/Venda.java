package com.example.apifinanceiro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    
    private boolean credito;
    private boolean debito;
    private double valor_total;
    private double icms;
    private int numero_parcelas;
    private double valor_parcela;
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataVenda;
    
    @Column(nullable = false)
    private String nomeMercadoria;
    
    @Column(nullable = false)
    private int quantidade;
    
    @Column(nullable = false)
    private double precoUnitario;

    @ManyToOne
    @JoinColumn(name = "fornecedor_venda_id")
    @JsonIgnore
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    public Venda() {
        this.dataVenda = LocalDateTime.now();
    }
    
    public Venda(boolean credito, boolean debito, double valor_total, double icms, 
                 int numero_parcelas, double valor_parcela, String nomeMercadoria, 
                 int quantidade, double precoUnitario, Fornecedor fornecedor, Cliente cliente) {
        this();
        this.credito = credito;
        this.debito = debito;
        this.valor_total = valor_total;
        this.icms = icms;
        this.numero_parcelas = numero_parcelas;
        this.valor_parcela = valor_parcela;
        this.nomeMercadoria = nomeMercadoria;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.fornecedor = fornecedor;
        this.cliente = cliente;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCredito() {
        return credito;
    }

    public void setCredito(boolean credito) {
        this.credito = credito;
    }

    public boolean isDebito() {
        return debito;
    }

    public void setDebito(boolean debito) {
        this.debito = debito;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public double getIcms() {
        return icms;
    }

    public void setIcms(double icms) {
        this.icms = icms;
    }

    public int getNumero_parcelas() {
        return numero_parcelas;
    }

    public void setNumero_parcelas(int numero_parcelas) {
        this.numero_parcelas = numero_parcelas;
    }

    public double getValor_parcela() {
        return valor_parcela;
    }

    public void setValor_parcela(double valor_parcela) {
        this.valor_parcela = valor_parcela;
    }
    
    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
    
    public String getNomeMercadoria() {
        return nomeMercadoria;
    }

    public void setNomeMercadoria(String nomeMercadoria) {
        this.nomeMercadoria = nomeMercadoria;
    }
    
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
