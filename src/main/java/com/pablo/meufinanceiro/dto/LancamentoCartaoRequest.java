package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record LancamentoCartaoRequest(
        Long usuarioId,
        String descricao,
        BigDecimal valor
) {}

