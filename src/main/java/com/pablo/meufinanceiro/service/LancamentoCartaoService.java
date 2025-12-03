package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.domain.LancamentoCartao;
import com.pablo.meufinanceiro.dto.LancamentoParceladoRequest;
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

    public void criarLancamentoParcelado(LancamentoParceladoRequest request) {

        CartaoCredito cartao = cartaoRepository.findById(request.cartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        Fatura faturaInicial = faturaRepository.findById(request.faturaIdInicial())
                .orElseThrow(() -> new RuntimeException("Fatura inicial não encontrada"));

        BigDecimal valorParcela = request.valorTotal()
                .divide(BigDecimal.valueOf(request.totalParcelas()), 2, BigDecimal.ROUND_HALF_UP);

        LocalDate dataBase = LocalDate.now();

        for (int i = 1; i <= request.totalParcelas(); i++) {

            LancamentoCartao lancamento = new LancamentoCartao();
            lancamento.setDescricao(request.descricao() + " (" + i + "/" + request.totalParcelas() + ")");
            lancamento.setValor(valorParcela);
            lancamento.setParcelado(true);
            lancamento.setNumeroParcela(i);
            lancamento.setTotalParcelas(request.totalParcelas());
            lancamento.setDataCompra(dataBase.plusMonths(i - 1));
            lancamento.setCartao(cartao);
            lancamento.setFatura(faturaInicial);

            lancamentoRepository.save(lancamento);

            faturaInicial.setValorTotal(
                    faturaInicial.getValorTotal().add(valorParcela)
            );
        }

        faturaRepository.save(faturaInicial);
    }

}
