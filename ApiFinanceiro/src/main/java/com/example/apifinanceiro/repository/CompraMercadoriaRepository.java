package com.example.apifinanceiro.repository;

import com.example.apifinanceiro.model.CompraMercadoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraMercadoriaRepository extends JpaRepository<CompraMercadoria, Integer> {
    @Query("SELECT c FROM CompraMercadoria c WHERE c.fornecedor.id = :fornecedorId")
    List<CompraMercadoria> findByFornecedorId(@Param("fornecedorId") int fornecedorId);
    
    List<CompraMercadoria> findByPagamentoVistaTrue();
    List<CompraMercadoria> findByPagamentoPrazoTrue();
} 