package com.pablo.meufinanceiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoReceitaRequest(
        Long usuarioId,
        String descricao,
        BigDecimal valor,
        LocalDate dataLancamento
) {
}
