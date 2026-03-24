package br.com.indra.jp_capacitacao_2026.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    /// TESTAR VALIDAÇÃO DE PREÇO
    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço não pode ser inferior a um centavo")
    private BigDecimal preco;

    @Column(name="codigo_barras")
    private String codigoBarras;

    /// TESTAR CAMPO MUITOS PRA UM
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
