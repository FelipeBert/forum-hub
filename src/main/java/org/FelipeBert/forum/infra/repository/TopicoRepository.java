package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.Status;
import org.FelipeBert.forum.domain.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    Page<Topico> findByStatusNot(Status status, Pageable paginacao);

    @Query("SELECT t FROM Topico t WHERE t.curso.id = :cursoId")
    Page<Topico> findTopicosByCursoId(@Param("cursoId") Long cursoId, Pageable paginacao);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.dataCriacao) = :year")
    Page<Topico> findTopicosByYear(@Param("year") int year, Pageable pagincao);

    @Query("SELECT t FROM Topico t ORDER BY t.dataCriacao ASC")
    List<Topico> findTop10ByOrderByDataCriacaoAsc(Pageable pageable);
}