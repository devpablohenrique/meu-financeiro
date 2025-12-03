package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.dto.FecharFaturaRequest;
import com.pablo.meufinanceiro.dto.PagarFaturaRequest;
import com.pablo.meufinanceiro.service.FaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/cartao/{cartaoId}")
    public List<Fatura> listarFaturasPorCartao(@PathVariable Long cartaoId) {
        return faturaService.listarPorCartao(cartaoId);
    }

    @PostMapping("/fechar")
    public void fechar(@RequestBody FecharFaturaRequest request) {
        faturaService.fecharFatura(request.faturaId());
    }

    @PostMapping("/pagar")
    public void pagar(@RequestBody PagarFaturaRequest request) {
        faturaService.pagarFatura(request.faturaId(), request.valorPago());
    }




}
