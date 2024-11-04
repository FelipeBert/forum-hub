package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
}
