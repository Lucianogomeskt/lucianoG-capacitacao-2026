package br.com.indra.jp_capacitacao_2026.repository;


import br.com.indra.jp_capacitacao_2026.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNameAndParentId(@NotBlank(message = "O nome da categoria é obrigatório") String name, Long aLong);

    List<Categoria> findAllByAtivoTrue();

    Optional<Categoria> findByIdAndAtivoTrue(Long id);
}