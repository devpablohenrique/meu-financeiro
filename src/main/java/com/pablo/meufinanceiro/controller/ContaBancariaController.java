package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.ContaBancaria;
import com.pablo.meufinanceiro.service.ContaBancariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaBancariaController {

    private final ContaBancariaService contaBancariaService;

    @PostMapping("/usuario/{usuarioId}")
    public ContaBancaria criarConta(
            @PathVariable Long usuarioId,
            @RequestBody ContaBancaria conta
    ) {
        return contaBancariaService.criarConta(usuarioId, conta);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ContaBancaria> listarContas(@PathVariable Long usuarioId) {
        return contaBancariaService.listarPorUsuario(usuarioId);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        contaBancariaService.deletar(id);
    }
}
