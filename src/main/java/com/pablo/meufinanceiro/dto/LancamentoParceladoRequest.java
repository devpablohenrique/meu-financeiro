package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record LancamentoParceladoRequest(
        Long cartaoId,
        Long usuarioId,
        String descricao,
        BigDecimal valorTotal,
        Integer totalParcelas
) {}

