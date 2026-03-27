package br.com.indra.jp_capacitacao_2026.service.dto;

import br.com.indra.jp_capacitacao_2026.enums.StatusCarrinho;

import java.math.BigDecimal;

public record CarrinhoResponseDTO(
        Long id,
        Long usuarioId,
        StatusCarrinho status,
        BigDecimal valorTotal
) {
}
