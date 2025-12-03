package com.pablo.meufinanceiro.repository;

import com.pablo.meufinanceiro.domain.LancamentoCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LancamentoCartaoRepository extends JpaRepository<LancamentoCartao, Long> {

    List<LancamentoCartao> findByFaturaId(Long faturaId);

}
