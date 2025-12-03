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
}
