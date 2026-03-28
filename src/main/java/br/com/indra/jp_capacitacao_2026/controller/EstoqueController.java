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
    @PutMapping("/ajustar/{id}")
    public ResponseEntity<Void> ajustar(@PathVariable Long id, @RequestParam int delta) {
        estoqueService.ajustarEstoque(id, delta);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Registra a devolução de um produto",
            description = "Incrementa o saldo do produto no Oracle e gera um log com o motivo DEVOLUCAO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PostMapping("/devolucao")
    public ResponseEntity<EstoqueResponseDTO> devolver(@RequestBody @Valid EstoqueRequestDTO dto) {
        return ResponseEntity.ok(estoqueService.registrarDevolucao(dto));
    }

    @Operation(
            summary = "Exclui um registro de movimentação",
            description = "Remove fisicamente o log de auditoria do Oracle. Cuidado: isso não estorna o saldo do produto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimentação excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "ID da movimentação não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueService.deletarMovimentacao(id);

        return ResponseEntity.noContent().build();
    }


}