package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.ContaBancaria;
import com.pablo.meufinanceiro.domain.Usuario;
import com.pablo.meufinanceiro.repository.ContaBancariaRepository;
import com.pablo.meufinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    private final ContaBancariaRepository contaBancariaRepository;
    private final UsuarioRepository usuarioRepository;

    public ContaBancaria criarConta(Long usuarioId, ContaBancaria conta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        conta.setUsuario(usuario);
        return contaBancariaRepository.save(conta);
    }

    public List<ContaBancaria> listarPorUsuario(Long usuarioId) {
        return contaBancariaRepository.findByUsuarioId(usuarioId);
    }

    public void deletar(Long id) {
        contaBancariaRepository.deleteById(id);
    }
}
