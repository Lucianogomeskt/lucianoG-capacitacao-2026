package br.com.indra.jp_capacitacao_2026.service.dto;

public record EstoqueRequestDTO(

        Long productId,
         Integer delta,
         String reason,
        String referenceId
) {
}