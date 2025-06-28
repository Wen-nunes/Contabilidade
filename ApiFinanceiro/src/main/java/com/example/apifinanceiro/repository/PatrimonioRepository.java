package com.example.apifinanceiro.repository;

import com.example.apifinanceiro.model.Patrimonio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrimonioRepository extends JpaRepository<Patrimonio, Integer> {
    Patrimonio findByFornecedorId(int fornecedorId);
} 