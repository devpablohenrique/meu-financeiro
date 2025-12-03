package com.pablo.meufinanceiro.repository;

import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.dto.DashboardMensalResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    Optional<Fatura> findByCartaoIdAndMesAndAno(Long cartaoId, Integer mes, Integer ano);

    List<Fatura> findByCartaoId(Long cartaoId);

    @Query("""
    SELECT COALESCE(SUM(f.valorTotal), 0)
    FROM Fatura f
    WHERE f.cartao.id = :cartaoId
      AND f.status = 'ABERTA'
""")
    BigDecimal totalEmAberto(Long cartaoId);

    @Query("""
    SELECT new com.pablo.meufinanceiro.dto.DashboardMensalResponse(
        f.cartao.id,
        f.cartao.nome,
        f.mes,
        f.ano,
        f.valorTotal
    )
    FROM Fatura f
    WHERE f.mes = :mes AND f.ano = :ano
""")
    List<DashboardMensalResponse> dashboardMensal(Integer mes, Integer ano);

}
