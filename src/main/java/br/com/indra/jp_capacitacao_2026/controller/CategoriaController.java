package br.com.indra.jp_capacitacao_2026.controller;



import br.com.indra.jp_capacitacao_2026.service.CategoriaService;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;


    @PostMapping("/cadastrar")
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO response = categoriaService.cadastrarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




}