package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.service.FaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faturas")
@RequiredArgsConstructor
public class FaturaController {

    private final FaturaService faturaService;

    @PostMapping("/cartao/{cartaoId}")
    public Fatura gerarFatura(
            @PathVariable Long cartaoId,
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return faturaService.gerarFatura(cartaoId, mes, ano);
    }
}
