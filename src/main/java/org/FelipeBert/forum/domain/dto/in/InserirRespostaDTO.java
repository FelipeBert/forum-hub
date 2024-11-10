package org.FelipeBert.forum.domain.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InserirRespostaDTO(
        @NotNull
        Long idTopico,
        @NotNull
        Long idAutor,
        @NotBlank
        String mensagem,
        @NotBlank
        String solucao

) {
}
