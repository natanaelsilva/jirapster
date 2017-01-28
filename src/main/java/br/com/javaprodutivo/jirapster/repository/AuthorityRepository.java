package br.com.javaprodutivo.jirapster.repository;

import br.com.javaprodutivo.jirapster.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
