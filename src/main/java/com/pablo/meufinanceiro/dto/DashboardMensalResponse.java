package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record DashboardMensalResponse(
        Long cartaoId,
        String nomeCartao,
        Integer mes,
        Integer ano,
        BigDecimal totalGasto
) {}
