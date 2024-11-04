package org.FelipeBert.forum.domain.validacoes;

import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.infra.repository.TopicoRepository;

public class ValidaTituloEMensagemDuplicados implements ValidadorCriacaoTopico{

    private TopicoRepository topicoRepository;

    public ValidaTituloEMensagemDuplicados(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @Override
    public void validar(CriarNovoTopicoDTO dados) {
        if(topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())){
            throw new IllegalArgumentException("Já existe um tópico com o mesmo título e mensagem.");
        }
    }
}
