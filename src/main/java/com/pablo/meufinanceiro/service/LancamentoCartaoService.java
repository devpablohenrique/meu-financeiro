package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.domain.LancamentoCartao;
import com.pablo.meufinanceiro.domain.enums.StatusFatura;
import com.pablo.meufinanceiro.dto.LancamentoParceladoRequest;
import com.pablo.meufinanceiro.repository.CartaoCreditoRepository;
import com.pablo.meufinanceiro.repository.FaturaRepository;
import com.pablo.meufinanceiro.repository.LancamentoCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

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

        // ✅ VALIDAR PELO LIMITE DISPONÍVEL (CORRETO)
        if (lancamento.getValor().compareTo(cartao.getLimiteDisponivel()) > 0) {
            throw new RuntimeException("Limite insuficiente no cartão");
        }

        // ✅ ABATER LIMITE DO CARTÃO
        cartao.setLimiteDisponivel(
                cartao.getLimiteDisponivel().subtract(lancamento.getValor())
        );
        cartaoRepository.save(cartao);

        lancamento.setCartao(cartao);
        lancamento.setFatura(fatura);
        lancamento.setDataCompra(LocalDate.now());
        lancamento.setParcelado(false);
        lancamento.setNumeroParcela(1);
        lancamento.setTotalParcelas(1);

        LancamentoCartao salvo = lancamentoRepository.save(lancamento);

        // ✅ ATUALIZAR TOTAL DA FATURA
        fatura.setValorTotal(
                fatura.getValorTotal().add(lancamento.getValor())
        );
        faturaRepository.save(fatura);

        return salvo;
    }

    // ✅ LANÇAMENTO PARCELADO
    public void criarLancamentoParcelado(LancamentoParceladoRequest request) {

        CartaoCredito cartao = cartaoRepository.findById(request.cartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        BigDecimal valorParcela = request.valorTotal()
                .divide(BigDecimal.valueOf(request.totalParcelas()), 2, RoundingMode.HALF_UP);

        LocalDate dataBase = LocalDate.now();

        for (int i = 1; i <= request.totalParcelas(); i++) {

            LocalDate dataParcela = dataBase.plusMonths(i - 1);
            int mes = dataParcela.getMonthValue();
            int ano = dataParcela.getYear();

            // 🔎 Buscar fatura do mês
            Fatura fatura = faturaRepository
                    .findByCartaoIdAndMesAndAno(cartao.getId(), mes, ano)
                    .orElseGet(() -> {
                        Fatura nova = new Fatura();
                        nova.setCartao(cartao);
                        nova.setMes(mes);
                        nova.setAno(ano);
                        nova.setValorTotal(BigDecimal.ZERO);
                        nova.setStatus(StatusFatura.ABERTA);
                        nova.setDataFechamento(LocalDate.of(ano, mes, cartao.getDiaFechamentoFatura()));
                        return faturaRepository.save(nova);
                    });

            LancamentoCartao lancamento = new LancamentoCartao();
            lancamento.setDescricao(request.descricao() + " (" + i + "/" + request.totalParcelas() + ")");
            lancamento.setValor(valorParcela);
            lancamento.setParcelado(true);
            lancamento.setNumeroParcela(i);
            lancamento.setTotalParcelas(request.totalParcelas());
            lancamento.setDataCompra(dataParcela);
            lancamento.setCartao(cartao);
            lancamento.setFatura(fatura);

            lancamentoRepository.save(lancamento);

            // Atualizar total da fatura
            fatura.setValorTotal(
                    fatura.getValorTotal().add(valorParcela)
            );

            faturaRepository.save(fatura);
        }

        // Atualizar limite disponível do cartão (valor TOTAL)
        cartao.setLimiteDisponivel(
                cartao.getLimiteDisponivel().subtract(request.valorTotal())
        );
        cartaoRepository.save(cartao);
    }

    public List<LancamentoCartao> listarPorFatura(Long faturaId) {

        return lancamentoRepository.findByFaturaId(faturaId);
    }

}
