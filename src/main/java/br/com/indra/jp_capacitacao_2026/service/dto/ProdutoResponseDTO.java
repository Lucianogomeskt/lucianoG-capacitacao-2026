package br.com.indra.jp_capacitacao_2026.service.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        BigDecimal preco,
        Integer quantidadeEstoque
) {

}