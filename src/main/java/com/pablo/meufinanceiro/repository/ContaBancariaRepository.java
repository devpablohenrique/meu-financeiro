package com.pablo.meufinanceiro.repository;

import com.pablo.meufinanceiro.domain.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

    List<ContaBancaria> findByUsuarioId(Long usuarioId);

}
