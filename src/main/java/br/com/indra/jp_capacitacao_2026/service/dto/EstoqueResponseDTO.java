package br.com.indra.jp_capacitacao_2026.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EstoqueResponseDTO(
        Long id,
        String nomeProduto,
        Integer quantidadeDelta,
        String motivo,
        LocalDateTime dataCriacao,
        Integer saldoAposMovimentacao
) {
}