package com.example.apifinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Patrimonio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    
    @Column(nullable = false)
    private double capitalSocial;
    
    @OneToOne
    @JoinColumn(name = "fornecedor_patrimonio_id")
    @JsonIgnore
    private Fornecedor fornecedor;
    
    public Patrimonio() {
    }
    
    public Patrimonio(double capitalSocial, Fornecedor fornecedor) {
        this.capitalSocial = capitalSocial;
        this.fornecedor = fornecedor;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public double getCapitalSocial() {
        return capitalSocial;
    }
    
    public void setCapitalSocial(double capitalSocial) {
        this.capitalSocial = capitalSocial;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    // Método para calcular o valor total dos bens (através do fornecedor)
    public double getValorTotalBens() {
        if (fornecedor == null || fornecedor.getBens() == null) return 0.0;
        return fornecedor.getBens().stream().mapToDouble(Bem::getValor).sum();
    }
    
    // Método para obter descrição dos bens como string (para compatibilidade)
    public String getDescricaoBens() {
        if (fornecedor == null || fornecedor.getBens() == null || fornecedor.getBens().isEmpty()) {
            return "Nenhum bem cadastrado";
        }
        StringBuilder descricao = new StringBuilder();
        for (Bem bem : fornecedor.getBens()) {
            descricao.append(bem.getNome()).append(" (R$ ").append(String.format("%.2f", bem.getValor())).append("), ");
        }
        return descricao.substring(0, descricao.length() - 2);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patrimonio that = (Patrimonio) o;
        return id == that.id && Double.compare(that.capitalSocial, capitalSocial) == 0 && Objects.equals(fornecedor, that.fornecedor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, capitalSocial, fornecedor);
    }
    
    @Override
    public String toString() {
        return "Patrimonio{" +
                "id=" + id +
                ", capitalSocial=" + capitalSocial +
                ", fornecedor=" + fornecedor +
                '}';
    }
} 