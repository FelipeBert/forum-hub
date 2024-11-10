package org.FelipeBert.forum.domain.dto.out;

import org.FelipeBert.forum.domain.model.Resposta;

import java.time.LocalDateTime;

public record DadosListagemRespostaDTO(
        Long id,
        Long idAutor,
        Long idTopico,
        String mensagem,
        String solucao,
        LocalDateTime data
) {

    public DadosListagemRespostaDTO(Resposta resposta){
        this(resposta.getId(), resposta.getAutor().getId(),
                resposta.getTopico().getId(), resposta.getMensagem(), resposta.getSolucao(), resposta.getDataCriacao());
    }
}
