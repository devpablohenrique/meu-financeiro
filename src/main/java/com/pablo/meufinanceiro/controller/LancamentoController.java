package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.Lancamento;
import com.pablo.meufinanceiro.dto.LancamentoCartaoRequest;
import com.pablo.meufinanceiro.dto.LancamentoParceladoRequest;
import com.pablo.meufinanceiro.dto.LancamentoSimplesRequest;
import com.pablo.meufinanceiro.dto.LancamentoReceitaRequest;
import com.pablo.meufinanceiro.service.LancamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    // ✅ LANÇAMENTO NO CARTÃO (À VISTA)
    @PostMapping("/cartao/{cartaoId}/fatura/{faturaId}")
    public Lancamento criarNoCartao(
            @PathVariable Long cartaoId,
            @PathVariable Long faturaId,
            @RequestBody LancamentoCartaoRequest request
    ) {
        return lancamentoService.criarLancamentoNoCartao(cartaoId, faturaId, request);
    }

    // ✅ LANÇAMENTO PARCELADO NO CARTÃO
    @PostMapping("/cartao/parcelado")
    public void criarParcelado(@RequestBody LancamentoParceladoRequest request) {
        lancamentoService.criarLancamentoParcelado(request);
    }

    // ✅ LANÇAMENTO FORA DO CARTÃO (DESPESA)
    @PostMapping("/simples")
    public Lancamento criarSimples(@RequestBody LancamentoSimplesRequest request) {
        return lancamentoService.criarDespesaSimples(request);
    }

    // ✅ NOVO — LANÇAMENTO DE RECEITA
    @PostMapping("/receita")
    public Lancamento criarReceita(@RequestBody LancamentoReceitaRequest request) {
        return lancamentoService.criarReceita(request);
    }

    // ✅ LISTAR LANÇAMENTOS POR FATURA
    @GetMapping("/fatura/{faturaId}")
    public List<Lancamento> listarPorFatura(@PathVariable Long faturaId) {
        return lancamentoService.listarPorFatura(faturaId);
    }

    // ✅ LISTAR LANÇAMENTOS POR CARTÃO
    @GetMapping("/cartao/{cartaoId}")
    public List<Lancamento> listarPorCartao(@PathVariable Long cartaoId) {
        return lancamentoService.listarPorCartao(cartaoId);
    }

    // ✅ LISTAR LANÇAMENTOS POR USUÁRIO (BASE DO DASHBOARD)
    @GetMapping("/usuario/{usuarioId}")
    public List<Lancamento> listarPorUsuario(@PathVariable Long usuarioId) {
        return lancamentoService.listarPorUsuario(usuarioId);
    }
}
