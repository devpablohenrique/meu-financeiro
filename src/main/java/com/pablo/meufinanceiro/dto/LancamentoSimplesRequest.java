package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record LancamentoSimplesRequest(
        Long usuarioId,
        String descricao,
        BigDecimal valor
) {}
