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

@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(description = "Endpoint para criar uma nova categoria", summary = "Criação de categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação: Verifique os campos obrigatórios"),
            @ApiResponse(responseCode = "409", description = "Conflito: Já existe uma categoria com este nome"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao processar o cadastro")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.cadastrarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lista todas as categorias", description = "Retorna 200 com a lista ou 204 se estiver vazio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhuma categoria cadastrada no sistema"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar categorias no servidor")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> getAll() {
        List<CategoriaResponseDTO> lista = categoriaService.findAll();
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
        CategoriaResponseDTO dto = categoriaService.findById(id);
        return ResponseEntity.ok(dto);
    }



}


