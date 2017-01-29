package br.com.javaprodutivo.jirapster.repository;

import br.com.javaprodutivo.jirapster.domain.Projeto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Projeto entity.
 */
@SuppressWarnings("unused")
public interface ProjetoRepository extends JpaRepository<Projeto,Long> {

}
