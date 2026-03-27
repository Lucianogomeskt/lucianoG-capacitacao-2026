package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.model.Estoque;
import br.com.indra.jp_capacitacao_2026.model.Produtos;
import br.com.indra.jp_capacitacao_2026.repository.EstoqueRepository;
import br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueResponseDTO;
import br.com.indra.jp_capacitacao_2026.enums.MotivoEstoque;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository movimentacaoRepository;
    private final ProdutoService produtoService;
    @Transactional
    public EstoqueResponseDTO registrarEstoque(@Valid EstoqueRequestDTO dto) {
        produtoService.ajustarEstoque(dto.productId(), dto.delta());

        Produtos produto = produtoService.getEntidadeById(dto.productId());

        Estoque movimentacao = new Estoque();

        movimentacaoRepository.save(movimentacao);

        return converterParaDTO(movimentacao, produto);
    }
    private EstoqueResponseDTO converterParaDTO(Estoque movimentacao, Produtos produto) {
        return new EstoqueResponseDTO(
                movimentacao.getId(),
                produto.getNome(),
                movimentacao.getDelta(),
                movimentacao.getReason().name(),
                movimentacao.getCreatedAt(),
                produto.getQuantidadeEstoque()
        );
    }
}





