package br.com.indra.jp_capacitacao_2026.controller;

import br.com.indra.jp_capacitacao_2026.service.CategoriaService;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Criação de categoria", description = "Endpoint para criar uma nova categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação: Verifique os campos obrigatórios"),
            @ApiResponse(responseCode = "409", description = "Conflito: Já existe uma categoria com este nome no mesmo nível"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.cadastrarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lista todas as categorias", description = "Retorna 200 com a lista ou 204 se estiver vazio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma categoria cadastrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar categorias")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> getAll() {
        List<CategoriaResponseDTO> lista = categoriaService.listarCategorias();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(lista);
        }
    }

    @Operation(summary = "Busca uma categoria por ID", description = "Retorna os detalhes de uma categoria ou 404 se não existir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada no banco Oracle"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getById(@PathVariable Long id) {
        CategoriaResponseDTO dto = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualiza uma categoria", description = "Retorna 200 após atualizar ou 404 se o ID não existir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado para atualização"),
            @ApiResponse(responseCode = "409", description = "Conflito: Nome já existente neste nível"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deleta uma categoria (Delete Físico)", description = "Retorna 204 após deletar ou 409 se houver produtos vinculados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado para exclusão"),
            @ApiResponse(responseCode = "409", description = "Erro: Existem produtos vinculados a esta categoria"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}