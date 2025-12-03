package com.pablo.meufinanceiro.repository;

import com.pablo.meufinanceiro.domain.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    Optional<Fatura> findByCartaoIdAndMesAndAno(Long cartaoId, Integer mes, Integer ano);

    List<Fatura> findByCartaoId(Long cartaoId);

}
