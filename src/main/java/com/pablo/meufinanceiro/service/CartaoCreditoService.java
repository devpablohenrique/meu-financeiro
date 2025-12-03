package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.CartaoCredito;
import com.pablo.meufinanceiro.domain.Usuario;
import com.pablo.meufinanceiro.repository.CartaoCreditoRepository;
import com.pablo.meufinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final UsuarioRepository usuarioRepository;

    public CartaoCredito criarCartao(Long usuarioId, CartaoCredito cartao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        cartao.setUsuario(usuario);
        return cartaoCreditoRepository.save(cartao);
    }

    public List<CartaoCredito> listarPorUsuario(Long usuarioId) {
        return cartaoCreditoRepository.findByUsuarioId(usuarioId);
    }

    public void deletar(Long id) {
        cartaoCreditoRepository.deleteById(id);
    }
}
