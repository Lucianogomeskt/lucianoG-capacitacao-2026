package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.EstoqueService;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimentação registrada e saldo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou saldo insuficiente"),
            @ApiResponse(responseCode = "404", description = "Produto informado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar a movimentação no banco")
    })
    @PostMapping("/registrar")
    public ResponseEntity<EstoqueResponseDTO> registrar(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para registro de estoque") EstoqueRequestDTO dto) {
        return ResponseEntity.ok(estoqueService.registrarEstoque(dto));
    }

    @Operation(
            summary = "Ajusta o saldo do produto",
            description = "Altera a quantidade em estoque diretamente no Oracle."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo ajustado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição ou saldo insuficiente"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado no sistema"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PatchMapping("/ajustar/{id}")
    public ResponseEntity<Void> ajustar(@PathVariable Long id, @RequestParam int delta) {
        estoqueService.ajustarEstoque(id, delta);
        return ResponseEntity.ok().build();
    }






}