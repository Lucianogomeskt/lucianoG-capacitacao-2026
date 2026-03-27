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
    @Operation(summary = "Remove um item do carrinho",
            description = "Exclui permanentemente um item (produto) do carrinho ativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao acessar o Oracle")
    })
    @DeleteMapping("/item/{carrinhoItemId}")
    public ResponseEntity<Void> removerItem(@PathVariable Long carrinhoItemId) {
        carrinhoService.removerItem(carrinhoItemId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Esvazia o carrinho do usuário",
            description = "Remove todos os itens (produtos) do carrinho sem excluir o carrinho em si.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Carrinho esvaziado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado")
    })
    @DeleteMapping("/esvaziar/{usuarioId}")
    public ResponseEntity<Void> esvaziar(@PathVariable Long usuarioId) {
        carrinhoService.esvaziarCarrinho(usuarioId);

        return ResponseEntity.noContent().build();
    }


}
