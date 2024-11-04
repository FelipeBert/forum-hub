package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
