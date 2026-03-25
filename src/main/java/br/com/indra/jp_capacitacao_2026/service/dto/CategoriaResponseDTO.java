package br.com.indra.jp_capacitacao_2026.service.dto;

import java.time.LocalDateTime;

public record CategoriaResponseDTO(
        Long id,
        String name,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
