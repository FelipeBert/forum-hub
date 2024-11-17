package org.FelipeBert.forum.domain.service;

import org.FelipeBert.forum.domain.dto.in.AtualizarRespostaDTO;
import org.FelipeBert.forum.domain.dto.in.InserirRespostaDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemRespostaDTO;
import org.FelipeBert.forum.domain.exceptions.EntidadeInativaException;
import org.FelipeBert.forum.domain.exceptions.EntidadeNaoEncontradaException;
import org.FelipeBert.forum.domain.model.Resposta;
import org.FelipeBert.forum.domain.model.Topico;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.infra.repository.RespostaRepository;
import org.FelipeBert.forum.infra.repository.TopicoRepository;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RespostaService {

    private RespostaRepository repository;
    private UsuarioRepository usuarioRepository;
    private TopicoRepository topicoRepository;

    public RespostaService(RespostaRepository repository, UsuarioRepository usuarioRepository, TopicoRepository topicoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.topicoRepository = topicoRepository;
    }

    @Transactional
    public DadosListagemRespostaDTO inserirResposta(InserirRespostaDTO dados){
        Usuario usuario = usuarioRepository.findById(dados.idAutor()).orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario"));
        Topico topico = topicoRepository.findById(dados.idTopico()).orElseThrow(() -> new EntidadeNaoEncontradaException("Topico"));

        Resposta resposta = new Resposta();

        resposta.setAutor(usuario);
        resposta.setTopico(topico);

        resposta.setMensagem(dados.mensagem());
        resposta.setSolucao(dados.solucao());

        resposta.setDataCriacao(LocalDateTime.now());

        topico.setRespostas(List.of(resposta));
        repository.save(resposta);

        return new DadosListagemRespostaDTO(resposta);
    }

    public DadosListagemRespostaDTO buscarPorId(Long id) {
        Resposta resposta = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Resposta"));
        if(!resposta.isAtivo()){
            throw new EntidadeInativaException();
        }

        return new DadosListagemRespostaDTO(resposta);
    }

    @Transactional
    public DadosListagemRespostaDTO atualizarResposta(AtualizarRespostaDTO dados) {
        Resposta resposta = repository.findById(dados.id()).orElseThrow(() -> new EntidadeNaoEncontradaException("Resposta"));

        if(!resposta.isAtivo()){
            throw new EntidadeInativaException();
        }

        resposta.setMensagem(dados.mensagem());
        resposta.setSolucao(dados.solucao());

        repository.save(resposta);

        return new DadosListagemRespostaDTO(resposta);
    }

    @Transactional
    public void excluirResposta(Long id) {
        Resposta resposta = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Resposta"));

        resposta.setAtivo(false);
    }
}
