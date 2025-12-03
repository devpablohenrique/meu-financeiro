package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.dto.SaldoCartaoResponse;
import com.pablo.meufinanceiro.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/cartao/{cartaoId}/saldo")
    public SaldoCartaoResponse saldoCartao(@PathVariable Long cartaoId) {
        return dashboardService.saldoConsolidado(cartaoId);
    }
}
