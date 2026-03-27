package br.com.indra.jp_capacitacao_2026.repository;

import br.com.indra.jp_capacitacao_2026.enums.StatusCarrinho;
import br.com.indra.jp_capacitacao_2026.model.Carrinho;

import java.util.Optional;

public interface CarrinhoRepository {
    Optional<Carrinho> findByUsuarioIdAndStatus(Long usuarioId, StatusCarrinho status);
}
