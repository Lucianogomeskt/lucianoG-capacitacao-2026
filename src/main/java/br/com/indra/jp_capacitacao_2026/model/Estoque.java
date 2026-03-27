package br.com.indra.jp_capacitacao_2026.model;

import br.com.indra.jp_capacitacao_2026.enums.MotivoEstoque;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produtos produto;

    @Column(name = "quantidade_delta", nullable = false)
    private Integer delta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoEstoque reason;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
/*
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.createdBy == null) {
            this.createdBy = "SISTEMA";

        }
    */
    }

