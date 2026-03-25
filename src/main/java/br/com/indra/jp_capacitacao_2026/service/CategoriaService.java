package br.com.indra.jp_capacitacao_2026.service;


import br.com.indra.jp_capacitacao_2026.exception.EntidadeConflitoException;
import br.com.indra.jp_capacitacao_2026.model.Categoria;
import br.com.indra.jp_capacitacao_2026.repository.CategoriaRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNameAndParentId(dto.name(), dto.parentId())) {
            throw new EntidadeConflitoException("Já existe uma categoria com o nome: " + dto.name());
        }

        Categoria categoria = converterParaEntidade(dto);

        Categoria salva = categoriaRepository.save(categoria);

        return converterParaDTO(salva);
    }

    private Categoria converterParaEntidade(CategoriaRequestDTO dto) {
        return Categoria.builder()
                .name(dto.name())
                .parentId(dto.parentId())
                .build();
    }

    private CategoriaResponseDTO converterParaDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getName(),
                categoria.getParentId(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );


    }
}

