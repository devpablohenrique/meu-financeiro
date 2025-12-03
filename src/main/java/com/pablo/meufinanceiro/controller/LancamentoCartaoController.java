package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.LancamentoCartao;
import com.pablo.meufinanceiro.service.LancamentoCartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoCartaoController {

    private final LancamentoCartaoService lancamentoService;

    @PostMapping("/cartao/{cartaoId}/fatura/{faturaId}")
    public LancamentoCartao criar(
            @PathVariable Long cartaoId,
            @PathVariable Long faturaId,
            @RequestBody LancamentoCartao lancamento
    ) {
        return lancamentoService.criarLancamento(cartaoId, faturaId, lancamento);
    }
}
