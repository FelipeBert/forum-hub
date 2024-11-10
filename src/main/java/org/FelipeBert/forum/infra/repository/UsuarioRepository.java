package org.FelipeBert.forum.infra.repository;

import org.FelipeBert.forum.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);

    Boolean existsByEmail(String email);
}
