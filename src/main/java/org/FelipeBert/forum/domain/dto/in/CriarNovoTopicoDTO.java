package org.FelipeBert.forum.domain.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarNovoTopicoDTO(
        @NotBlank
        String titulo,
        @NotBlank
        String mensagem,
        @NotNull
        Long idAutor,
        @NotNull
        Long idCurso) {
}
