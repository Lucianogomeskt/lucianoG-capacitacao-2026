package br.com.indra.jp_capacitacao_2026.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HistoricoProdutoDTO(
        UUID id,
        String produto,
        BigDecimal precoAntigo,
        BigDecimal precoNovo,
        LocalDateTime dataRegistro
) {}