package com.example.apifinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    
    @Column(nullable = false)
    private String nome;
    
    private String endereco;
    
    @Column(nullable = false)
    private double capitalSocial;

    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Venda> vendas;
    
    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CompraMercadoria> comprasMercadorias;
    
    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Bem> bens;
    
    @OneToOne(mappedBy = "fornecedor", cascade = CascadeType.ALL)
    @JsonIgnore
    private Patrimonio patrimonio;

    public Fornecedor() {
    }
    
    public Fornecedor(int id, String nome, List<Venda> vendas, String endereco, double capitalSocial) {
        this.id = id;
        this.nome = nome;
        this.vendas = vendas;
        this.endereco = endereco;
        this.capitalSocial = capitalSocial;
    }

    public Fornecedor(String nome, String endereco, List<Venda> vendas, double capitalSocial) {
        this.nome = nome;
        this.endereco = endereco;
        this.vendas = vendas;
        this.capitalSocial = capitalSocial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public double getCapitalSocial() {
        return capitalSocial;
    }

    public void setCapitalSocial(double capitalSocial) {
        this.capitalSocial = capitalSocial;
    }
    
    public List<CompraMercadoria> getComprasMercadorias() {
        return comprasMercadorias;
    }

    public void setComprasMercadorias(List<CompraMercadoria> comprasMercadorias) {
        this.comprasMercadorias = comprasMercadorias;
    }
    
    public List<Bem> getBens() {
        return bens;
    }

    public void setBens(List<Bem> bens) {
        this.bens = bens;
    }
    
    public Patrimonio getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(Patrimonio patrimonio) {
        this.patrimonio = patrimonio;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return id == that.id && Double.compare(that.capitalSocial, capitalSocial) == 0 && Objects.equals(nome, that.nome) && Objects.equals(endereco, that.endereco) && Objects.equals(vendas, that.vendas) && Objects.equals(comprasMercadorias, that.comprasMercadorias) && Objects.equals(bens, that.bens) && Objects.equals(patrimonio, that.patrimonio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, endereco, capitalSocial, vendas, comprasMercadorias, bens, patrimonio);
    }
}
