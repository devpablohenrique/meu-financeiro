package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.dto.SaldoCartaoResponse;
import com.pablo.meufinanceiro.repository.CartaoCreditoRepository;
import com.pablo.meufinanceiro.repository.FaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CartaoCreditoRepository cartaoRepository;
    private final FaturaRepository faturaRepository;

    public SaldoCartaoResponse saldoConsolidado(Long cartaoId) {

        CartaoCredito cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        BigDecimal totalEmAberto = faturaRepository.totalEmAberto(cartaoId);

        LocalDate hoje = LocalDate.now();
        BigDecimal gastoMesAtual = faturaRepository
                .findByCartaoIdAndMesAndAno(cartaoId, hoje.getMonthValue(), hoje.getYear())
                .map(f -> f.getValorTotal())
                .orElse(BigDecimal.ZERO);

        return new SaldoCartaoResponse(
                cartao.getId(),
                cartao.getNome(),
                cartao.getLimiteTotal(),
                cartao.getLimiteDisponivel(),
                totalEmAberto,
                gastoMesAtual
        );
    }
}
