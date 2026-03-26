package br.com.indra.jp_capacitacao_2026.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01")
        BigDecimal preco,

        String codigoBarras,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoriaId
) {}