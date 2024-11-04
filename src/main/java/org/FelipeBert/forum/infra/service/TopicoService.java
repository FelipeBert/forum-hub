package org.FelipeBert.forum.infra.service;

import jakarta.persistence.EntityNotFoundException;
import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.domain.model.Status;
import org.FelipeBert.forum.domain.model.Topico;
import org.FelipeBert.forum.domain.validacoes.ValidadorCriacaoTopico;
import org.FelipeBert.forum.infra.repository.CursoRepository;
import org.FelipeBert.forum.infra.repository.TopicoRepository;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicoService {

    private TopicoRepository repository;
    private UsuarioRepository usuarioRepository;
    private CursoRepository cursoRepository;
    private List<ValidadorCriacaoTopico> validador;

    public TopicoService(TopicoRepository repository, UsuarioRepository usuarioRepository,
                         CursoRepository cursoRepository, List<ValidadorCriacaoTopico> validador) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.validador = validador;
    }

    @Transactional
    public void criarNovoTopico(CriarNovoTopicoDTO dados){
        validador.forEach(v -> v.validar(dados));

        var autor = usuarioRepository.findById(dados.idAutor())
                .orElseThrow(() -> new EntityNotFoundException("Id não corresponde a Nenhum Usuario"));

        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new EntityNotFoundException("Id não corresponde a Nenhum Curso"));

        Topico novoTopico = new Topico();
        novoTopico.setMensagem(dados.mensagem());
        novoTopico.setTitulo(dados.titulo());
        novoTopico.setAutor(autor);
        novoTopico.setCurso(curso);
        novoTopico.setDataCriacao(LocalDateTime.now());
        novoTopico.setStatus(Status.NAORESPONDIDO);

        repository.save(novoTopico);
    }
}
