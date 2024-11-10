package org.FelipeBert.forum.domain.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarRespostaDTO(
        @NotNull
        Long id,
        @NotBlank
        String mensagem,
        @NotBlank
        String solucao
) {
}
