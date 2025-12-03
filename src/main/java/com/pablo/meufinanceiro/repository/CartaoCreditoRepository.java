package com.pablo.meufinanceiro.repository;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {

    List<CartaoCredito> findByUsuarioId(Long usuarioId);

}
