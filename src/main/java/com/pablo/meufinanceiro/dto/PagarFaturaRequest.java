package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;

public record PagarFaturaRequest(
        Long faturaId,
        BigDecimal valorPago
) {}
