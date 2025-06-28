package com.example.apifinanceiro.repository;

import com.example.apifinanceiro.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
    @Query("SELECT v FROM Venda v WHERE v.fornecedor.id = :fornecedorId")
    List<Venda> findByFornecedorId(@Param("fornecedorId") int fornecedorId);

    List<Venda> findByClienteId(int clienteId);
}