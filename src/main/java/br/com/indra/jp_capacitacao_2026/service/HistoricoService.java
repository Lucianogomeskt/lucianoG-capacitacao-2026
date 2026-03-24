package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.model.HistoricoPreco;
import br.com.indra.jp_capacitacao_2026.repository.HistoricoPrecoRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.HistoricoProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    public HistoricoProdutoDTO getHistoricoByProdutoId(Long produtoId) {
        Set<HistoricoPreco> historicoPrecos = historicoPrecoRepository.findByProdutosId(produtoId);


                //.stream().flatMap(/*desenvolveria o mapeamento*/).toList();
        /**
         * Existe várias forma de se mappear, map, flatmap, for, stream, mapperstruct, projection
         */
              /*

        return new HistoricoProdutoDTO(
                historicoPrecos.getId(),
                historicoPrecos.getProdutos().getNome(),
                historicoPrecos.getPrecoAntigo(),
                historicoPrecos.getPrecoNovo(),
                historicoPrecos.getDataAlteracao()
        );
       */

//
        return null;
    }
}
