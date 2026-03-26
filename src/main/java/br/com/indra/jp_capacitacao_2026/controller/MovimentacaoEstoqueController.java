package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.MovimentacaoEstoqueService;
import br.com.indra.jp_capacitacao_2026.service.dto.MovimentacaoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.MovimentacaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Gerenciamento de entradas e saídas de produtos")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoService;

    @Operation(
            summary = "Registra entrada ou saída de estoque",
            description = "Atualiza o saldo do produto no Oracle e gera um log de auditoria na tabela de movimentação."
    )
    @PostMapping("/movimentar")
    public ResponseEntity<MovimentacaoResponseDTO> movimentar(@RequestBody @Valid MovimentacaoRequestDTO dto) {
        return ResponseEntity.ok(movimentacaoService.gerenciarEstoque(dto));
    }
}