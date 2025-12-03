package com.pablo.meufinanceiro.domain;

import com.pablo.meufinanceiro.domain.enums.StatusFatura;
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
@Table(name = "faturas")
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFatura status;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private CartaoCredito cartao;

    @Column(nullable = false)
    private LocalDate dataFechamento;
}
