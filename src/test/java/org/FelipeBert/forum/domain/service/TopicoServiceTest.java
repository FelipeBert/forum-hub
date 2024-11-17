package org.FelipeBert.forum.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.FelipeBert.forum.domain.dto.in.AtualizarTopicoDTO;
import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemTopicosDTO;
import org.FelipeBert.forum.domain.exceptions.ParametrosAtualizacaoInvalidosException;
import org.FelipeBert.forum.domain.model.Curso;
import org.FelipeBert.forum.domain.model.Status;
import org.FelipeBert.forum.domain.model.Topico;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.domain.validacoes.ValidadorCriacaoTopico;
import org.FelipeBert.forum.infra.repository.CursoRepository;
import org.FelipeBert.forum.infra.repository.TopicoRepository;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.FelipeBert.forum.domain.exceptions.EntidadeNaoEncontradaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class TopicoServiceTest {

    private TopicoRepository topicoRepository;
    private CursoRepository cursoRepository;
    private UsuarioRepository usuarioRepository;
    private List<ValidadorCriacaoTopico> validadores;
    private TopicoService topicoService;

    @BeforeEach
    void setUp(){
        topicoRepository = mock(TopicoRepository.class);
        cursoRepository = mock(CursoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        validadores = new ArrayList<>();

        topicoService = new TopicoService(topicoRepository, usuarioRepository, cursoRepository, validadores);
    }

    @Test
    @DisplayName("Deve criar um novo tópico com sucesso quando os dados são válidos")
    void criarNovoTopicoCenario1(){
        CriarNovoTopicoDTO dados = new CriarNovoTopicoDTO("Titulo", "Mensagem", 1L, 1L);
        Usuario mockAutor = new Usuario();
        Curso mockCurso = new Curso();

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockAutor));
        Mockito.when(cursoRepository.findById(1L)).thenReturn(Optional.of(mockCurso));

        Topico novoTopico = topicoService.criarNovoTopico(dados);

        assertNotNull(novoTopico);
        assertEquals("Titulo", novoTopico.getTitulo());
        assertEquals("Mensagem", novoTopico.getMensagem());
        assertEquals(mockAutor, novoTopico.getAutor());
        assertEquals(mockCurso, novoTopico.getCurso());
        assertEquals(Status.NAORESPONDIDO, novoTopico.getStatus());
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradaException quando o usuário não for encontrado")
    void criarNovoTopicoCenario2(){
        CriarNovoTopicoDTO dados = new CriarNovoTopicoDTO("Titulo", "Mensagem", 1L, 1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            topicoService.criarNovoTopico(dados);
        });
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradaException quando o curso não for encontrado")
    void criarNovoTopicoCenario3(){
        CriarNovoTopicoDTO dados = new CriarNovoTopicoDTO("Titulo", "Mensagem", 1L, 1L);
        Usuario mockAutor = new Usuario();

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockAutor));
        Mockito.when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            topicoService.criarNovoTopico(dados);
        });
    }

    @Test
    @DisplayName("Deve atualizar um tópico existente e retornar os dados atualizados quando os parâmetros são válidos")
    void atualizarTopicoCenario1(){
        Long id = 1L;
        AtualizarTopicoDTO dados = new AtualizarTopicoDTO("New Title", "New Message");
        Topico existingTopico = new Topico();
        existingTopico.setId(id);
        existingTopico.setTitulo("Old Title");
        existingTopico.setMensagem("Old Message");
        existingTopico.setAutor(new Usuario());
        existingTopico.setCurso(new Curso());

        Mockito.when(topicoRepository.findById(id)).thenReturn(Optional.of(existingTopico));

        DadosListagemTopicosDTO result = topicoService.atualizarTopico(id, dados);

        Assertions.assertEquals("New Title", result.titulo());
        Assertions.assertEquals("New Message", result.mensagem());
        Mockito.verify(topicoRepository).save(existingTopico);
    }

    @Test
    @DisplayName("Deve lançar ParametrosAtualizacaoInvalidosException quando o título e a mensagem são nulos")
    void atualizarTopicoCenario2(){
        AtualizarTopicoDTO dados = new AtualizarTopicoDTO(null, null);

        assertThrows(ParametrosAtualizacaoInvalidosException.class, () -> {
            topicoService.atualizarTopico(1L, dados);
        });
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradaException quando o tópico a ser atualizado não for encontrado")
    void atualizarTopicoCenario3(){
        Long id = 999L;
        AtualizarTopicoDTO dados = new AtualizarTopicoDTO("New Title", "New Message");

        Mockito.when(topicoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            topicoService.atualizarTopico(id, dados);
        });
    }

    @Test
    @DisplayName("Deve alterar o status do tópico para EXCLUIDO quando o ID é válido")
    void excluirTopicoCenario1(){
        Long id = 1L;
        Topico existingTopico = new Topico();
        existingTopico.setId(id);
        existingTopico.setStatus(Status.NAORESPONDIDO);

        Mockito.when(topicoRepository.findById(id)).thenReturn(Optional.of(existingTopico));

        topicoService.excluirTopico(id);

        assertEquals(Status.EXCLUIDO, existingTopico.getStatus());
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradaException ao tentar excluir um tópico que não existe")
    void excluirTopicoCenario2(){
        Long id = 999L;

        Mockito.when(topicoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            topicoService.excluirTopico(id);
        });
    }

}