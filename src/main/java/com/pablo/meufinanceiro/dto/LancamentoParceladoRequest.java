package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record LancamentoParceladoRequest(
        String descricao,
        BigDecimal valorTotal,
        Integer totalParcelas,
        Long cartaoId,
        Long faturaIdInicial
) {}
