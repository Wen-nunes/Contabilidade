package com.example.apifinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    @Column(nullable = false)
    private String nome;
    private String endereco;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Venda> vendas;

    public Cliente(String nome, int id, String endereco, List<Venda> vendas) {
        this.nome = nome;
        this.id = id;
        this.endereco = endereco;
        this.vendas = vendas;
    }

    public Cliente(String nome, String endereco, List<Venda> vendas) {
        this.nome = nome;
        this.endereco = endereco;
        this.vendas = vendas;
    }

    public Cliente() {
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id && Objects.equals(nome, cliente.nome) && Objects.equals(endereco, cliente.endereco) && Objects.equals(vendas, cliente.vendas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, endereco, vendas);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", vendas=" + vendas +
                '}';
    }
}
