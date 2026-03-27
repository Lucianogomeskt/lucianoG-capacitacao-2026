package br.com.indra.jp_capacitacao_2026.service;


import br.com.indra.jp_capacitacao_2026.exception.RecursoNaoEncontradoException;
import br.com.indra.jp_capacitacao_2026.model.Categoria;
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
            ProdutoResponseDTO dto = converterParaDTO(produto);
            dtos.add(dto);
        }

        return dtos;
    }

    public ProdutoResponseDTO getById(Long id) {
        Optional<Produtos> optionalProduto = produtosRepository.findByIdAndAtivoTrue(id);
        if (optionalProduto.isPresent()) {
            Produtos produto = optionalProduto.get();
            return converterParaDTO(produto);
        } else {
            throw new RecursoNaoEncontradoException("Produto ID " + id + " não encontrado ou está inativo.");
        }
    }

    public Produtos atualiza(Produtos produto) {
        return produtosRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtosRepository.deleteById(id);
    }



    public Produtos atualizaPreco(Long id, BigDecimal preco) {
//        Produtos produto = produtosRepository.findById(id).get();
        final var produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setPreco(preco);
/*
        final var historico = new HistoricoPreco();
        historico.setPrecoAntigo(produto.getPreco());
        historico.setProdutos(produto);
        historico.setPrecoNovo(preco);

        //Código abaixo pode ser substituido por @CreationTimestamp
//        historico.setDataAlteracao(LocalDateTime.now());
        historicoPrecoRepository.save(historico);
        return produtosRepository.saveAndFlush(produto);

        //Exemplo de não se fazer por gerar retrabalho
          final var historicoNovo = historicoPrecoRepository.findById(historico.getId()).get();
          historicoNovo.setPrecoNovo(preco);
          historicoPrecoRepository.save(historicoNovo);

         * get na tabela produtos para novo preço

//        return produto;
*/
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
 * Rastreabilidade
 * 1 - Criar um log
 * 2 - Adicionar em tabela historico de preços valores old e new
 * para cada produto atualizado
 * 3 - Antes de atualizar a tabela de produto, pegar o valor atual da tabela e adiconar
 * na tabela historico
 * 4 - Pegar novo valor da tabela e adicionar na tabela historico
 * 5 - Sempre na tabela, adicionar novo registro após atualizar tabela de produto
 * Estrutura da tabela historico de preços
 * id
 * id_produto
 * preco_antigo
 * preco_novo
 * data_alteracao
 */