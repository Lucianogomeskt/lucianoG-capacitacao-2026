package br.com.indra.jp_capacitacao_2026.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricoProdutoDTO {

    private UUID id;
    private String produto;
    private BigDecimal precoAntigo;
    private BigDecimal precoNovo;
    private LocalDateTime dataRegistro;

}
