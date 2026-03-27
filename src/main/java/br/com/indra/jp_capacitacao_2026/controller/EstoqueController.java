package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.EstoqueService;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueResponseDTO;
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
public class EstoqueController {

    private final EstoqueService estoqueService;

    @Operation(
            summary = "Registra entrada ou saída de estoque",
            description = "Atualiza o saldo do produto no Oracle e gera um log de auditoria na tabela de movimentação."
    )
    @PostMapping("/registrar")
    public ResponseEntity<EstoqueResponseDTO> registrar(@RequestBody @Valid EstoqueRequestDTO dto) {

        return ResponseEntity.ok(estoqueService.registrarEstoque(dto));
    }
}