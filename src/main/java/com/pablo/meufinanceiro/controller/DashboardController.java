package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.dto.DashboardMensalResponse;
import com.pablo.meufinanceiro.dto.SaldoCartaoResponse;
import com.pablo.meufinanceiro.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/cartao/{cartaoId}/saldo")
    public SaldoCartaoResponse saldoCartao(@PathVariable Long cartaoId) {
        return dashboardService.saldoConsolidado(cartaoId);
    }

    @GetMapping("/mensal")
    public List<DashboardMensalResponse> dashboardMensal(
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return dashboardService.resumoMensal(mes, ano);
    }

}
