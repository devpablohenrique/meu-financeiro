package com.pablo.meufinanceiro.controller;

import com.pablo.meufinanceiro.domain.Usuario;
import com.pablo.meufinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/teste")
@RequiredArgsConstructor
public class TesteController {

    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public Usuario criarUsuario() {
        Usuario usuario = Usuario.builder()
                .nome("Pablo")
                .email("pablo@email.com")
                .senha("123456")
                .dataCriacao(LocalDateTime.now())
                .build();

        return usuarioRepository.save(usuario);
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
}
