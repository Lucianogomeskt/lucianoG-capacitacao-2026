package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.model.Estoque;
import br.com.indra.jp_capacitacao_2026.model.Produtos;
import br.com.indra.jp_capacitacao_2026.repository.EstoqueRepository;
import br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.EstoqueResponseDTO;
import br.com.indra.jp_capacitacao_2026.enums.MotivoEstoque; // Certifique-se de que o import do seu Enum está aqui
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository movimentacaoRepository;
    private final ProdutosRepository produtosRepository;

    @Transactional
    public EstoqueResponseDTO registrarEstoque(@Valid EstoqueRequestDTO dto) {

        Produtos produto = produtosRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int valorMovimentado = dto.delta();
        int novoSaldo = produto.getQuantidadeEstoque() + valorMovimentado;

        if (novoSaldo < 0) {
            throw new RuntimeException("Saldo insuficiente!");
        }

        Estoque movimentacao = new Estoque();
        movimentacao.setProduto(produto);
        movimentacao.setDelta(valorMovimentado);

        movimentacao.setReason(MotivoEstoque.valueOf(dto.reason().toUpperCase()));

        movimentacao.setReferenceId(dto.referenceId());
        movimentacao.setCreatedBy("movimentacao_estoque");

        movimentacaoRepository.save(movimentacao);

        produto.setQuantidadeEstoque(novoSaldo);
        produtosRepository.save(produto);

        return new EstoqueResponseDTO(
                movimentacao.getId(),
                produto.getNome(),
                movimentacao.getDelta(),
                movimentacao.getReason().name(),
                movimentacao.getCreatedAt(),
                novoSaldo
        );
    }
}