package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.CarrinhoService;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinho")
@RequiredArgsConstructor
@Tag(name = "Carrinho", description = "Endpoints para gerenciamento do carrinho de compras")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @Operation(summary = "Busca ou cria um carrinho ativo",
            description = "Retorna o carrinho ATIVO do usuário. Se não existir, um novo será criado automaticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho recuperado ou criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor Oracle")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarrinhoResponseDTO> buscarOuCriar(@PathVariable Long usuarioId) {
        CarrinhoResponseDTO response = carrinhoService.buscarOuCriarCarrinho(usuarioId);
        return ResponseEntity.ok(response);
    }
}
