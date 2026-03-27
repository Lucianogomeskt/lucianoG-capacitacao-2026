package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.exception.EntidadeConflitoException;
import br.com.indra.jp_capacitacao_2026.exception.RecursoNaoEncontradoException;
import br.com.indra.jp_capacitacao_2026.model.Categoria;
import br.com.indra.jp_capacitacao_2026.model.HistoricoPreco;
import br.com.indra.jp_capacitacao_2026.model.Produtos;
import br.com.indra.jp_capacitacao_2026.repository.CategoriaRepository;
import br.com.indra.jp_capacitacao_2026.repository.EstoqueRepository;
import br.com.indra.jp_capacitacao_2026.repository.HistoricoPrecoRepository;
import br.com.indra.jp_capacitacao_2026.repository.ProdutosRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.ProdutoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.ProdutoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstoqueRepository estoqueRepository;

    @Transactional
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não é possível cadastrar: Categoria ID " + dto.categoriaId() + " não encontrada no sistema."));

        Produtos produto = converterParaEntidade(dto, categoria);
        Produtos produtoSalvo = produtosRepository.save(produto);

        return converterParaDTO(produtoSalvo);
    }

    public List<ProdutoResponseDTO> getAll() {
        List<Produtos> entidades = produtosRepository.findAllByAtivoTrue();
        List<ProdutoResponseDTO> dtos = new ArrayList<>();

        for (Produtos produto : entidades) {
            dtos.add(converterParaDTO(produto));
        }
        return dtos;
    }

    public ProdutoResponseDTO getById(Long id) {
        Optional<Produtos> optionalProduto = produtosRepository.findByIdAndAtivoTrue(id);
        if (optionalProduto.isPresent()) {
            return converterParaDTO(optionalProduto.get());
        } else {
            throw new RecursoNaoEncontradoException("Produto ID " + id + " não encontrado ou está inativo.");
        }
    }

    @Transactional
    public void ajustarEstoque(Long id, Integer quantidadeMovimentada) {
        Produtos produto = produtosRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto ID " + id + " não encontrado ou inativo."));

        int novaQuantidade = produto.getQuantidadeEstoque() + quantidadeMovimentada;

        if (novaQuantidade < 0) {
            throw new EntidadeConflitoException("Operação negada: Estoque insuficiente! Saldo atual: " + produto.getQuantidadeEstoque());
        }

        produto.setQuantidadeEstoque(novaQuantidade);
        produtosRepository.save(produto);
    }

    @Transactional
    public void desativarProduto(Long id) {
        Produtos produto = produtosRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado ou já está inativo."));
        produto.setAtivo(false);
        produtosRepository.save(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizarPreco(Long id, BigDecimal novoPreco) {
        Produtos produto = produtosRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado para atualização de preço."));

        BigDecimal precoAntigo = produto.getPreco();

        produto.setPreco(novoPreco);
        produtosRepository.save(produto);

        HistoricoPreco historico = new HistoricoPreco();
        historico.setProdutos(produto);
        historico.setPrecoAntigo(precoAntigo);
        historico.setPrecoNovo(novoPreco);

        historicoPrecoRepository.save(historico);

        return converterParaDTO(produto);
    }

    private Produtos converterParaEntidade(ProdutoRequestDTO dto, Categoria categoria) {
        Produtos produto = new Produtos();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setCodigoBarras(dto.codigoBarras());
        produto.setCategory(categoria);
        produto.setAtivo(true);
        produto.setQuantidadeEstoque(0);
        return produto;
    }

    private ProdutoResponseDTO converterParaDTO(Produtos produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getQuantidadeEstoque()
        );
    }
}

/***
 * Rastreabilidade implementada em atualizarPreco:
 * 1 - Busca produto ativo
 * 2 - Armazena preço antigo (old value)
 * 3 - Atualiza com novo preço (new value)
 * 4 - Salva registro na tabela HistoricoPreco relacionando o produto
 * * Estrutura da tabela historico de preços no Oracle:
 * ID (PK)
 * PRODUTO_ID (FK)
 * PRECO_ANTIGO
 * PRECO_NOVO
 * DATA_ALTERACAO
 */