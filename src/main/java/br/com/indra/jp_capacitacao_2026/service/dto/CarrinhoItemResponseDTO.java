package br.com.indra.jp_capacitacao_2026.service.dto;

import java.math.BigDecimal;

public record CarrinhoItemResponseDTO(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal priceSnapshot,
        BigDecimal subtotal
) {

}
