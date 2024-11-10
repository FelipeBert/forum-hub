package org.FelipeBert.forum.domain.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AtualizarSenhaDTO(
        @Email
        String email,
        @NotBlank
        String senhaAntiga,
        @NotBlank
        String senhaNova
) {
}
