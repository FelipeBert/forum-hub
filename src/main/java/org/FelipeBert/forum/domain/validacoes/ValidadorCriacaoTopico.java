package org.FelipeBert.forum.domain.validacoes;

import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;

public interface ValidadorCriacaoTopico {

    void validar(CriarNovoTopicoDTO dados);
}
