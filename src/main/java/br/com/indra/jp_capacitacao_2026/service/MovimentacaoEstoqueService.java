package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.model.MovimentacaoEstoque;
import br.com.indra.jp_capacitacao_2026.model.Produtos;
import br.com.indra.jp_capacitacao_2026.repository.MovimentacaoEstoqueRepository;
import br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.MovimentacaoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.MovimentacaoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutosRepository produtosRepository;

    @Transactional
    public MovimentacaoResponseDTO gerenciarEstoque(MovimentacaoRequestDTO dto) {
        Produtos produto = produtosRepository.findById(dto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int saldoAtual = (produto.getQuantidadeEstoque() != null) ? produto.getQuantidadeEstoque() : 0;
        int novoSaldo = saldoAtual + dto.quantidade();

        if (novoSaldo < 0) {
            throw new RuntimeException("Saldo insuficiente! Estoque atual: " + saldoAtual);
        }

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidadeDelta(dto.quantidade());
        movimentacao.setMotivo(dto.motivo());
        movimentacaoRepository.save(movimentacao);

        produto.setQuantidadeEstoque(novoSaldo);
        produtosRepository.save(produto);

        return converterParaResponseDTO(movimentacao, novoSaldo);
    }

    private MovimentacaoResponseDTO converterParaResponseDTO(MovimentacaoEstoque m, int saldoFinal) {
        return MovimentacaoResponseDTO.builder()
                .id(m.getId())
                .nomeProduto(m.getProduto().getNome())
                .quantidadeDelta(m.getQuantidadeDelta())
                .motivo(m.getMotivo())
                .dataCriacao(m.getCriadoEm())
                .saldoAposMovimentacao(saldoFinal)
                .build();
    }
}