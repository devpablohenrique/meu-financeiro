package com.pablo.meufinanceiro.service;

import com.pablo.meufinanceiro.domain.Usuario;
import com.pablo.meufinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Criar usuário
    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Listar todos
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Deletar
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
