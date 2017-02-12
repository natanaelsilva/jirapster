package br.com.javaprodutivo.jirapster.repository;

import br.com.javaprodutivo.jirapster.domain.Tarefa;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tarefa entity.
 */
@SuppressWarnings("unused")
public interface TarefaRepository extends JpaRepository<Tarefa,Long> {

    @Query("select tarefa from Tarefa tarefa where tarefa.usuarioReq.login = ?#{principal.username}")
    List<Tarefa> findByUsuarioReqIsCurrentUser();

    @Query("select tarefa from Tarefa tarefa where tarefa.usuarioDev.login = ?#{principal.username}")
    List<Tarefa> findByUsuarioDevIsCurrentUser();

}
