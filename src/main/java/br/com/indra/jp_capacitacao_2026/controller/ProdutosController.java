package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.model.Produtos;
import br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import br.com.indra.jp_capacitacao_2026.service.ProdutosService;
import br.com.indra.jp_capacitacao_2026.service.dto.ProdutoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutosService produtosService;

    @Operation(
            summary = "Cadastra um novo produto",
            description = "Cria um produto no banco Oracle associado a uma categoria. A categoria deve estar ativa."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos (validação de campos)"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou está inativa no sistema"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar a operação no banco")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtosService.cadastrarProduto(dto));
    }

    /**
     * GET
     * localhost:9090/produtos
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Produtos>> getAll(){
        return ResponseEntity.ok(produtosService.getAll());
    }

    /**
     * GET
     * localhost:9090/produtos/1
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produtos> getById(@PathVariable Long id){
        return ResponseEntity.ok(produtosService.getById(id));
    }

    @PutMapping("/atualiza")
    public ResponseEntity<Produtos> atualizarProduto(@RequestParam Long id,
                                                     @RequestBody Produtos produto){
        return ResponseEntity.ok(produtosService.atualiza(produto));
    }

    @PatchMapping("/atualiza-preco/{id}")
    public ResponseEntity<Produtos> atualizarProdutoParcial(@PathVariable Long id,
                                                     @RequestParam BigDecimal preco) {
        return ResponseEntity.ok(produtosService.atualizaPreco(id, preco));
    }

    //Mudar para delete lógico
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtosService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
