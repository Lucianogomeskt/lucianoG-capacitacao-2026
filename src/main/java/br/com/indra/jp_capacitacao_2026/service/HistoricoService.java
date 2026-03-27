package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.model.HistoricoPreco;
import br.com.indra.jp_capacitacao_2026.repository.HistoricoPrecoRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.HistoricoProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    @Transactional(readOnly = true)
    public List<HistoricoProdutoDTO> getHistoricoByProdutoId(Long produtoId) {
        Set<HistoricoPreco> historicoPrecos = historicoPrecoRepository.findByProdutosId(produtoId);

        return historicoPrecos.stream()
                .map(h -> new HistoricoProdutoDTO(
                        h.getId(),
                        h.getProdutos().getNome(),
                        h.getPrecoAntigo(),
                        h.getPrecoNovo(),
                        h.getDataAlteracao()
                ))
                .sorted((h1, h2) -> h2.dataRegistro().compareTo(h1.dataRegistro()))
                .toList();
    }
}