package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.domain.Fatura;
import com.pablo.meufinanceiro.domain.Lancamento;
import com.pablo.meufinanceiro.domain.Usuario;
import com.pablo.meufinanceiro.domain.enums.StatusFatura;
import com.pablo.meufinanceiro.dto.LancamentoCartaoRequest;
import com.pablo.meufinanceiro.dto.LancamentoParceladoRequest;
import com.pablo.meufinanceiro.dto.LancamentoSimplesRequest;
import com.pablo.meufinanceiro.repository.CartaoCreditoRepository;
import com.pablo.meufinanceiro.repository.FaturaRepository;
import com.pablo.meufinanceiro.repository.LancamentoRepository;
import com.pablo.meufinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final FaturaRepository faturaRepository;
    private final CartaoCreditoRepository cartaoRepository;
    private final UsuarioRepository usuarioRepository;

    // ✅ LANÇAMENTO NO CARTÃO (À VISTA)
    public Lancamento criarLancamentoNoCartao(
            Long cartaoId,
            Long faturaId,
            LancamentoCartaoRequest request
    ) {
        CartaoCredito cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(request.descricao());
        lancamento.setValor(request.valor());
        lancamento.setUsuario(usuario);
        lancamento.setCartao(cartao);
        lancamento.setFatura(fatura);
        lancamento.setDataLancamento(LocalDate.now());
        lancamento.setParcelado(false);
        lancamento.setNumeroParcela(1);
        lancamento.setTotalParcelas(1);
        lancamento.setRecorrente(false);

        Lancamento salvo = lancamentoRepository.save(lancamento);

        fatura.setValorTotal(
                fatura.getValorTotal().add(lancamento.getValor())
        );
        faturaRepository.save(fatura);

        cartao.setLimiteDisponivel(
                cartao.getLimiteDisponivel().subtract(lancamento.getValor())
        );
        cartaoRepository.save(cartao);

        return salvo;
    }

    // ✅ LANÇAMENTO PARCELADO NO CARTÃO
    public void criarLancamentoParcelado(LancamentoParceladoRequest request) {

        CartaoCredito cartao = cartaoRepository.findById(request.cartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (request.valorTotal().compareTo(cartao.getLimiteDisponivel()) > 0) {
            throw new RuntimeException("Limite insuficiente no cartão");
        }

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BigDecimal valorParcela = request.valorTotal()
                .divide(BigDecimal.valueOf(request.totalParcelas()), 2, RoundingMode.HALF_UP);

        LocalDate dataBase = LocalDate.now();

        for (int i = 1; i <= request.totalParcelas(); i++) {

            LocalDate dataParcela = dataBase.plusMonths(i - 1);
            int mes = dataParcela.getMonthValue();
            int ano = dataParcela.getYear();

            Fatura fatura = faturaRepository
                    .findByCartaoIdAndMesAndAno(cartao.getId(), mes, ano)
                    .orElseGet(() -> {
                        Fatura nova = new Fatura();
                        nova.setCartao(cartao);
                        nova.setMes(mes);
                        nova.setAno(ano);
                        nova.setValorTotal(BigDecimal.ZERO);
                        nova.setStatus(StatusFatura.ABERTA);
                        nova.setDataFechamento(
                                LocalDate.of(ano, mes, cartao.getDiaFechamentoFatura())
                        );
                        return faturaRepository.save(nova);
                    });

            Lancamento lancamento = new Lancamento();
            lancamento.setDescricao(request.descricao() + " (" + i + "/" + request.totalParcelas() + ")");
            lancamento.setValor(valorParcela);
            lancamento.setParcelado(true);
            lancamento.setNumeroParcela(i);
            lancamento.setTotalParcelas(request.totalParcelas());
            lancamento.setDataLancamento(dataParcela);
            lancamento.setCartao(cartao);
            lancamento.setFatura(fatura);
            lancamento.setRecorrente(false);
            lancamento.setUsuario(usuario);

            lancamentoRepository.save(lancamento);

            fatura.setValorTotal(
                    fatura.getValorTotal().add(valorParcela)
            );
            faturaRepository.save(fatura);
        }

        cartao.setLimiteDisponivel(
                cartao.getLimiteDisponivel().subtract(request.valorTotal())
        );
        cartaoRepository.save(cartao);
    }

    // ✅ LANÇAMENTO FORA DO CARTÃO (Luz, aluguel, pix, etc)
    public Lancamento criarLancamentoSimples(LancamentoSimplesRequest request) {

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Lancamento lancamento = new Lancamento();
        lancamento.setUsuario(usuario);
        lancamento.setDescricao(request.descricao());
        lancamento.setValor(request.valor());

        lancamento.setCartao(null);
        lancamento.setFatura(null);
        lancamento.setParcelado(false);
        lancamento.setRecorrente(false);
        lancamento.setNumeroParcela(1);
        lancamento.setTotalParcelas(1);
        lancamento.setDataLancamento(LocalDate.now());

        return lancamentoRepository.save(lancamento);
    }


    // ✅ LISTAR POR FATURA
    public List<Lancamento> listarPorFatura(Long faturaId) {
        return lancamentoRepository.findByFaturaId(faturaId);
    }

    // ✅ LISTAR POR CARTÃO
    public List<Lancamento> listarPorCartao(Long cartaoId) {
        return lancamentoRepository.findByCartaoId(cartaoId);
    }

    // ✅ LISTAR POR USUÁRIO (BASE DO DASHBOARD)
    public List<Lancamento> listarPorUsuario(Long usuarioId) {
        return lancamentoRepository.findByUsuarioId(usuarioId);
    }
}
