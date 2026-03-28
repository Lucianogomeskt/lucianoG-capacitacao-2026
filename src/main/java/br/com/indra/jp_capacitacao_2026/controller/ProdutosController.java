package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.ProdutoService;
import br.com.indra.jp_capacitacao_2026.service.dto.ProdutoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Gerenciamento completo de produtos e estoque no banco Oracle")
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutoService produtoService;

    @Operation(summary = "Cadastra um novo produto", description = "Cria um produto ativo vinculado a uma categoria existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos enviados"),
            @ApiResponse(responseCode = "404", description = "Categoria informada não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrarProduto(dto));
    }

    @Operation(summary = "Lista todos os produtos ativos", description = "Retorna apenas produtos com status ativo=true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum produto ativo encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao consultar o banco de dados")
    })
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> lista = produtoService.getAll();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca produto por ID", description = "Retorna os detalhes se o produto existir e estiver ativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado ou inativo"),
            @ApiResponse(description = "ID inválido", responseCode = "400")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.getById(id));
    }

    @Operation(summary = "Movimentação de estoque", description = "Soma (valor positivo) ou subtrai (valor negativo) do estoque atual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estoque atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Estoque insuficiente para a saída solicitada")
    })
    @PutMapping("/{id}/estoque")
    public ResponseEntity<Void> ajustarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        produtoService.ajustarEstoque(id, quantidade);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza preço com rastreabilidade", description = "Altera o preço e gera log automático na tabela de Histórico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Preço alterado e histórico registrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para atualização")
    })
    @PutMapping("/{id}/preco")
    public ResponseEntity<ProdutoResponseDTO> atualizarPreco(@PathVariable Long id, @RequestParam BigDecimal preco) {
        return ResponseEntity.ok(produtoService.atualizarPreco(id, preco));
    }

    @Operation(summary = "Desativa um produto (Delete Lógico)", description = "Altera o status 'ativo' para false para preservar o histórico de preços.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para desativação")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.desativarProduto(id);
        return ResponseEntity.noContent().build();
    }
}