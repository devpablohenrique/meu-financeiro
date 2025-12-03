package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.service.CartaoCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoCreditoController {

    private final CartaoCreditoService cartaoCreditoService;

    @PostMapping("/usuario/{usuarioId}")
    public CartaoCredito criarCartao(
            @PathVariable Long usuarioId,
            @RequestBody CartaoCredito cartao
    ) {
        return cartaoCreditoService.criarCartao(usuarioId, cartao);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CartaoCredito> listar(@PathVariable Long usuarioId) {
        return cartaoCreditoService.listarPorUsuario(usuarioId);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        cartaoCreditoService.deletar(id);
    }
}
