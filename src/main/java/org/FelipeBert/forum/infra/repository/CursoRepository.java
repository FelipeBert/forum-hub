package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
