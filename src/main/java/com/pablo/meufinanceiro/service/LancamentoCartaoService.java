package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.domain.LancamentoCartao;
import com.pablo.meufinanceiro.repository.CartaoCreditoRepository;
import com.pablo.meufinanceiro.repository.FaturaRepository;
import com.pablo.meufinanceiro.repository.LancamentoCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LancamentoCartaoService {

    private final LancamentoCartaoRepository lancamentoRepository;
    private final FaturaRepository faturaRepository;
    private final CartaoCreditoRepository cartaoRepository;

    public LancamentoCartao criarLancamento(
            Long cartaoId,
            Long faturaId,
            LancamentoCartao lancamento
    ) {
        CartaoCredito cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        // Validar limite
        if (lancamento.getValor().compareTo(cartao.getLimiteTotal()) > 0) {
            throw new RuntimeException("Valor maior que o limite do cartão");
        }

        lancamento.setCartao(cartao);
        lancamento.setFatura(fatura);
        lancamento.setDataCompra(LocalDate.now());

        LancamentoCartao salvo = lancamentoRepository.save(lancamento);

        // Atualizar total da fatura
        fatura.setValorTotal(
                fatura.getValorTotal().add(lancamento.getValor())
        );
        faturaRepository.save(fatura);

        return salvo;
    }
}
