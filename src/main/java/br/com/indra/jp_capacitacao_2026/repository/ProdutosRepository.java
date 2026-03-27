package br.com.indra.jp_capacitacao_2026.repository;

import br.com.indra.jp_capacitacao_2026.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findAllByAtivoTrue();

    Optional<Produtos> findByIdAndAtivoTrue(Long id);

    boolean existsByCategoriaId(Long categoriaId);

}