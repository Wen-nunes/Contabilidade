package com.example.apifinanceiro.repository;

import com.example.apifinanceiro.model.Bem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BemRepository extends JpaRepository<Bem, Integer> {
    @Query("SELECT b FROM Bem b WHERE b.fornecedor.id = :fornecedorId")
    List<Bem> findByFornecedorId(@Param("fornecedorId") int fornecedorId);
} 