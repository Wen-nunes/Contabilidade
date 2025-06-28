package com.example.apifinanceiro.repository;

import com.example.apifinanceiro.model.Mercadoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoriaRepository extends JpaRepository<Mercadoria, Integer> {
    @Query("SELECT m FROM Mercadoria m WHERE m.nome = :nome")
    Mercadoria findByNome(@Param("nome") String nome);
} 