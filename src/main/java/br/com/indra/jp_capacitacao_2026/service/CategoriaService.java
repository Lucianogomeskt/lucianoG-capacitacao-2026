package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.exception.RecursoNaoEncontradoException;
import br.com.indra.jp_capacitacao_2026.exception.EntidadeConflitoException;
import br.com.indra.jp_capacitacao_2026.model.Categoria;
import br.com.indra.jp_capacitacao_2026.repository.CategoriaRepository;
import br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CategoriaResponseDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutosRepository produtosRepository;

    @Transactional
    public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new EntidadeConflitoException("Nome da categoria deve ser informado!");
        }

        if (categoriaRepository.existsByNameIgnoreCaseAndParentId(dto.name(), dto.parentId())) {
            throw new EntidadeConflitoException("Já existe uma categoria com esse nome neste nível!");
        }

        Categoria nova = converterParaEntidade(dto);
        Categoria salva = categoriaRepository.save(nova);
        return converterParaDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        List<Categoria> entidades = categoriaRepository.findAll();
        List<CategoriaResponseDTO> dtos = new ArrayList<>();

        for (Categoria entidade : entidades) {
            dtos.add(converterParaDTO(entidade));
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        return converterParaDTO(categoria);
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria existente = buscarEntidadePorId(id);

        if (dto.name() == null || dto.name().isBlank()) {
            throw new EntidadeConflitoException("O nome deve ser informado para atualizar!");
        }

        if (categoriaRepository.existsByNameIgnoreCaseAndParentIdAndIdNot(dto.name(), dto.parentId(), id)) {
            throw new EntidadeConflitoException("Já existe outra categoria com este nome neste nível!");
        }

        existente.setName(dto.name());
        existente.setParentId(dto.parentId());

        Categoria atualizada = categoriaRepository.save(existente);
        return converterParaDTO(atualizada);
    }

    @Transactional
    public void deletar(Long id) {
        buscarEntidadePorId(id);

        if (produtosRepository.existsByCategoriaId(id)) {
            throw new EntidadeConflitoException("Negado: Existem produtos vinculados a esta categoria");
        }

        categoriaRepository.deleteById(id);
    }

    protected Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
    }

    private Categoria converterParaEntidade(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setName(dto.name());
        categoria.setParentId(dto.parentId());
        return categoria;
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