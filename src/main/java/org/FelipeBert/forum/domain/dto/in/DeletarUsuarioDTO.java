package org.FelipeBert.forum.domain.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeletarUsuarioDTO(
        @Email
        String email,
        @NotBlank
        String senha
) {
}
