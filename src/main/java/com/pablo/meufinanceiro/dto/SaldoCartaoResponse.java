package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record SaldoCartaoResponse(
        Long cartaoId,
        String nomeCartao,
        BigDecimal limiteTotal,
        BigDecimal limiteDisponivel,
        BigDecimal totalEmAberto,
        BigDecimal gastoMesAtual
) {}
