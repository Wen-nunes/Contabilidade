package com.example.apifinanceiro.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Mercadoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    private String nome;
    private String descricao;
    private float preco_de_compra;
    private float preco_venda;
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    public Mercadoria(int id, String nome, String descricao, float preco_de_compra, float preco_venda, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco_de_compra = preco_de_compra;
        this.preco_venda = preco_venda;
        this.quantidade = quantidade;
    }

    public Mercadoria(String nome, String descricao, float preco_de_compra, float preco_venda, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco_de_compra = preco_de_compra;
        this.preco_venda = preco_venda;
        this.quantidade = quantidade;
    }

    public Mercadoria() {
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPreco_de_compra() {
        return preco_de_compra;
    }

    public void setPreco_de_compra(float preco_de_compra) {
        this.preco_de_compra = preco_de_compra;
    }

    public float getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(float preco_venda) {
        this.preco_venda = preco_venda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mercadoria that = (Mercadoria) o;
        return id == that.id && Float.compare(preco_de_compra, that.preco_de_compra) == 0 && Float.compare(preco_venda, that.preco_venda) == 0 && quantidade == that.quantidade && Objects.equals(nome, that.nome) && Objects.equals(descricao, that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, preco_de_compra, preco_venda, quantidade);
    }

    @Override
    public String toString() {
        return "Mercadoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco_de_compra=" + preco_de_compra +
                ", preco_venda=" + preco_venda +
                ", quantidade=" + quantidade +
                '}';
    }
}
