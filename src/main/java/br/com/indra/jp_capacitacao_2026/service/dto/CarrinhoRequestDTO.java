package br.com.indra.jp_capacitacao_2026.service.dto;


import jakarta.validation.constraints.NotNull;

public record CarrinhoRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório para abrir um carrinho.")
        Long usuarioId
) {
}
