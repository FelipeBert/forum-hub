package org.FelipeBert.forum.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.FelipeBert.forum.domain.dto.in.AtualizarTopicoDTO;
import org.FelipeBert.forum.domain.dto.in.BuscaAnoDTO;
import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemTopicosDTO;
import org.FelipeBert.forum.domain.exceptions.EntidadeNaoEncontradaException;
import org.FelipeBert.forum.domain.exceptions.ParametrosAtualizacaoInvalidosException;
import org.FelipeBert.forum.domain.model.Status;
import org.FelipeBert.forum.domain.model.Topico;
import org.FelipeBert.forum.domain.validacoes.ValidadorCriacaoTopico;
import org.FelipeBert.forum.infra.repository.CursoRepository;
import org.FelipeBert.forum.infra.repository.TopicoRepository;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public Topico criarNovoTopico(CriarNovoTopicoDTO dados){
        validarDados(dados);

        var autor = usuarioRepository.findById(dados.idAutor())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario"));

        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Curso"));

        Topico novoTopico = new Topico();
        novoTopico.setMensagem(dados.mensagem());
        novoTopico.setTitulo(dados.titulo());
        novoTopico.setAutor(autor);
        novoTopico.setCurso(curso);
        novoTopico.setDataCriacao(LocalDateTime.now());
        novoTopico.setStatus(Status.NAORESPONDIDO);

        repository.save(novoTopico);
        return novoTopico;
    }

    @Transactional
    public DadosListagemTopicosDTO atualizarTopico(Long id, AtualizarTopicoDTO dados) {
        if(dados.titulo() == null && dados.mensagem() == null){
            throw new ParametrosAtualizacaoInvalidosException();
        }
        Topico topico = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Topico"));

        CriarNovoTopicoDTO dadosListagem = new CriarNovoTopicoDTO(dados.titulo(), dados.mensagem(), topico.getAutor().getId(), topico.getCurso().getId());
        validarDados(dadosListagem);

        if(dados.titulo() != null){
            topico.setTitulo(dados.titulo());
        }
        if(dados.mensagem() != null){
            topico.setMensagem(dados.mensagem());
        }
        repository.save(topico);

        return new DadosListagemTopicosDTO(topico);
    }

    @Transactional
    public void excluirTopico(Long id) {
        Topico topico = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Topico"));
        topico.setStatus(Status.EXCLUIDO);
    }


    public Page<DadosListagemTopicosDTO> listarTopicos(Pageable paginacao) {
        var dados = repository.findByStatusNot(Status.EXCLUIDO, paginacao);

        Page<DadosListagemTopicosDTO> topicos = convertePageDeTopicosParaDTO(dados);

        return topicos;
    }

    public Page<DadosListagemTopicosDTO> listarTopicosPorCurso(Long id, Pageable paginacao) {
        var dados = repository.findTopicosByCursoId(id, paginacao);

        Page<DadosListagemTopicosDTO> topicos = convertePageDeTopicosParaDTO(dados);

        return topicos;
    }

    public Page<DadosListagemTopicosDTO> listarTopicosPorAno(BuscaAnoDTO dado, Pageable paginacao) {
        var dados = repository.findTopicosByYear(dado.ano(), paginacao);

        Page<DadosListagemTopicosDTO> topicos = convertePageDeTopicosParaDTO(dados);

        return topicos;
    }

    public List<DadosListagemTopicosDTO> listarDezTopicosPorDataCriacao(Pageable paginacao) {
        var dados = repository.findTop10ByOrderByDataCriacaoAsc(paginacao);

        return dados.stream().map(DadosListagemTopicosDTO::new).collect(Collectors.toList());
    }

    public DadosListagemTopicosDTO buscarTopicoPorId(Long id){
        Topico topico = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new DadosListagemTopicosDTO(topico);
    }

    private Page<DadosListagemTopicosDTO> convertePageDeTopicosParaDTO(Page<Topico> dados){
        return dados.map(DadosListagemTopicosDTO::new);
    }

    private void validarDados(CriarNovoTopicoDTO dados){
        validador.forEach(v -> v.validar(dados));
    }
}
