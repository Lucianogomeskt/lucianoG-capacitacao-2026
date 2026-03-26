package br.com.indra.jp_capacitacao_2026.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovimentacaoResponseDTO {

    private Long id;
    private String nomeProduto;
    private Integer quantidadeDelta;
    private String motivo;
    private LocalDateTime dataCriacao;
    private Integer saldoAposMovimentacao;

}
