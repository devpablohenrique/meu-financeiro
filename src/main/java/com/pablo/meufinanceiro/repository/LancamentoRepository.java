package com.pablo.meufinanceiro.repository;

import com.pablo.meufinanceiro.domain.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    // Lançamentos por fatura (cartão)
    List<Lancamento> findByFaturaId(Long faturaId);

    // Lançamentos por cartão (independente da fatura)
    List<Lancamento> findByCartaoId(Long cartaoId);

    // Lançamentos por usuário
    List<Lancamento> findByUsuarioId(Long usuarioId);

    // Lançamentos por usuário e mês (base do dashboard)
    List<Lancamento> findByUsuarioIdAndDataLancamentoBetween(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim
    );

    // Apenas recorrentes
    List<Lancamento> findByRecorrenteTrue();
}
