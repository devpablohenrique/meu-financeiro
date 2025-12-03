package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.domain.enums.StatusFatura;
import com.pablo.meufinanceiro.repository.CartaoCreditoRepository;
import com.pablo.meufinanceiro.repository.FaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final CartaoCreditoRepository cartaoCreditoRepository;

    public Fatura gerarFatura(Long cartaoId, Integer mes, Integer ano) {

        CartaoCredito cartao = cartaoCreditoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        return faturaRepository.findByCartaoIdAndMesAndAno(cartaoId, mes, ano)
                .orElseGet(() -> {
                    Fatura fatura = Fatura.builder()
                            .mes(mes)
                            .ano(ano)
                            .valorTotal(BigDecimal.ZERO)
                            .status(StatusFatura.ABERTA)
                            .cartao(cartao)
                            .dataFechamento(LocalDate.of(ano, mes, cartao.getDiaFechamentoFatura()))
                            .build();

                    return faturaRepository.save(fatura);
                });
    }

    public List<Fatura> listarPorCartao(Long cartaoId) {
        return faturaRepository.findByCartaoId(cartaoId);
    }

    public void fecharFatura(Long faturaId) {

        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        if (fatura.getStatus() != StatusFatura.ABERTA) {
            throw new RuntimeException("Apenas faturas ABERTAS podem ser fechadas.");
        }

        fatura.setStatus(StatusFatura.FECHADA);
        fatura.setDataFechamento(LocalDate.now());

        faturaRepository.save(fatura);
    }

    public void pagarFatura(Long faturaId, BigDecimal valorPago) {

        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        if (fatura.getStatus() != StatusFatura.FECHADA) {
            throw new RuntimeException("Apenas faturas FECHADAS podem ser pagas.");
        }

        if (valorPago.compareTo(fatura.getValorTotal()) != 0) {
            throw new RuntimeException("O valor pago deve ser exatamente o valor total da fatura.");
        }

        CartaoCredito cartao = fatura.getCartao();

        cartao.setLimiteDisponivel(
                cartao.getLimiteDisponivel().add(valorPago)
        );

        fatura.setStatus(StatusFatura.PAGA);
        fatura.setDataPagamento(LocalDate.now());

        cartaoCreditoRepository.save(cartao);
        faturaRepository.save(fatura);
    }


}
