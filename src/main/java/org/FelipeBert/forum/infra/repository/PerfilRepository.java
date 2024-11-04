package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}
