package br.com.indra.jp_capacitacao_2026.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequestDTO(
        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        Integer quantidade,

        @NotBlank(message = "O motivo deve ser informado")
        String motivo
) {}
