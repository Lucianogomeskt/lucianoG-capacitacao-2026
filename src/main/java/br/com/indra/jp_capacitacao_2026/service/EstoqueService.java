package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.enums.MotivoEstoque;
import br.com.indra.jp_capacitacao_2026.exception.ProdutoNaoEncontradoException;
import br.com.indra.jp_capacitacao_2026.exception.SaldoInsuficienteException;
import br.com.indra.jp_capacitacao_2026.model.Estoque;
import br.com.indra.jp_capacitacao_2026.model.Produtos;
import br.com.indra.jp_capacitacao_2026.repository.EstoqueRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueResponseDTO;
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

    @Transactional
    public void ajustarEstoque(Long produtoId, int delta) {
        Produtos produto = produtoService.getEntidadeById(produtoId);

        int novoSaldo = produto.getQuantidadeEstoque() + delta;

        if (novoSaldo < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para o produto: " + produto.getNome());
        }

        produto.setQuantidadeEstoque(novoSaldo);
    }

    @Transactional
    public EstoqueResponseDTO registrarDevolucao(@Valid EstoqueRequestDTO dto) {
        int deltaPositivo = Math.abs(dto.delta());

        produtoService.ajustarEstoque(dto.productId(), deltaPositivo);

        Produtos produto = produtoService.getEntidadeById(dto.productId());

        Estoque movimentacao = new Estoque();
        movimentacao.setProduto(produto);
        movimentacao.setDelta(deltaPositivo);
        movimentacao.setReason(MotivoEstoque.DEVOLUCAO);
        movimentacao.setReferenceId(dto.referenceId());

        movimentacaoRepository.save(movimentacao);

        return converterParaDTO(movimentacao, produto);
    }

    @Transactional
    public void deletarMovimentacao(Long id) {
        Estoque movimentacao = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Movimentação de estoque ID " + id + " não encontrada."));

        movimentacaoRepository.delete(movimentacao);
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





