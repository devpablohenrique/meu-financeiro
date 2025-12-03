package com.pablo.meufinanceiro.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lancamentos_cartao")
public class LancamentoCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate dataCompra;

    @Column(nullable = false)
    private boolean parcelado;

    @Column(nullable = false)
    private Integer numeroParcela;

    @Column(nullable = false)
    private Integer totalParcelas;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private CartaoCredito cartao;

    @ManyToOne
    @JoinColumn(name = "fatura_id", nullable = false)
    private Fatura fatura;

    // GARANTE PADRÃO PARA LANÇAMENTO À VISTA
    @PrePersist
    public void prePersist() {
        if (!this.parcelado) {
            this.numeroParcela = 1;
            this.totalParcelas = 1;
        }
    }

}


