package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.CarrinhoService;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/buscar-ou-criar")
    public ResponseEntity<CarrinhoResponseDTO> buscarOuCriar(@RequestBody CarrinhoRequestDTO dto) {
        CarrinhoResponseDTO response = carrinhoService.buscarOuCriarCarrinho(dto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Visualiza o carrinho ativo",
            description = "Retorna os detalhes e o total do carrinho ATIVO do usuário informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho localizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado para este usuário"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor Oracle")
    })
    @GetMapping("/usuario/{usuarioId}/ver")
    public ResponseEntity<CarrinhoResponseDTO> verCarrinho(@PathVariable Long usuarioId) {
        CarrinhoResponseDTO response = carrinhoService.verCarrinho(usuarioId);
        return ResponseEntity.ok(response);
    }


}
