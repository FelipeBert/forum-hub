package org.FelipeBert.forum.domain.dto.out;

import org.FelipeBert.forum.domain.model.Topico;

import java.time.LocalDateTime;

public record DadosListagemTopicosDTO(String titulo, String mensagem, LocalDateTime data, String autor, String curso) {

    public DadosListagemTopicosDTO(Topico dados){
        this(dados.getTitulo(), dados.getMensagem(), dados.getDataCriacao(),
                dados.getAutor().getNome(), dados.getCurso().getNome());
    }
}