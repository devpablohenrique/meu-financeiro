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
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate dataLancamento;

    // =========================
    // RECORRÊNCIA
    // =========================
    @Column(nullable = false)
    private Boolean recorrente;

    private Integer diaVencimento; // apenas se recorrente

    private LocalDate dataInicioRecorrencia;

    private LocalDate dataFimRecorrencia;

    // =========================
    // PARCELAMENTO
    // =========================
    @Column(nullable = false)
    private Boolean parcelado;

    @Column(nullable = false)
    private Integer numeroParcela;

    @Column(nullable = false)
    private Integer totalParcelas;

    // =========================
    // RELACIONAMENTOS
    // =========================

    // Se for null → lançamento fora do cartão (ex: luz, aluguel, pix)
    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private CartaoCredito cartao;

    // Se for null → não pertence a fatura
    @ManyToOne
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // =========================
    // PADRÕES AUTOMÁTICOS
    // =========================
    @PrePersist
    public void prePersist() {

        if (parcelado == null) parcelado = false;
        if (recorrente == null) recorrente = false;

        if (!parcelado) {
            this.numeroParcela = 1;
            this.totalParcelas = 1;
        }

        if (this.dataLancamento == null) {
            this.dataLancamento = LocalDate.now();
        }
    }
}
