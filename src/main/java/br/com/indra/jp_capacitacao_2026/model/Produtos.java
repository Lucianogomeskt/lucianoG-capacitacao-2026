package br.com.indra.jp_capacitacao_2026.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço não pode ser inferior a um centavo")
    private BigDecimal preco;

    @Column(name="codigo_barras")
    private String codigoBarras;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria category;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque = 0;

}
