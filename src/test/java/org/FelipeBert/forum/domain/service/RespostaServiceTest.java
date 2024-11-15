package org.FelipeBert.forum.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.FelipeBert.forum.domain.dto.in.AtualizarRespostaDTO;
import org.FelipeBert.forum.domain.dto.in.InserirRespostaDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemRespostaDTO;
import org.FelipeBert.forum.domain.model.Resposta;
import org.FelipeBert.forum.domain.model.Topico;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.infra.repository.RespostaRepository;
import org.FelipeBert.forum.infra.repository.TopicoRepository;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class RespostaServiceTest {

    private RespostaRepository respostaRepository;
    private UsuarioRepository usuarioRepository;
    private TopicoRepository topicoRepository;
    private RespostaService respostaService;

    @BeforeEach
    void setUp(){
        respostaRepository = mock(RespostaRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        topicoRepository = mock(TopicoRepository.class);

        respostaService = new RespostaService(respostaRepository, usuarioRepository, topicoRepository);
    }

    @Test
    @DisplayName("Deve inserir uma nova resposta ao tópico quando IDs de usuário e tópico forem válidos")
    void inserirRespostaCenario1(){
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        Topico topico = new Topico();
        topico.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(topicoRepository.findById(id)).thenReturn(Optional.of(topico));

        InserirRespostaDTO inserirRespostaDTO = new InserirRespostaDTO(id, id, "Mensagem", "Solucao");

        DadosListagemRespostaDTO resultado = respostaService.inserirResposta(inserirRespostaDTO);

        assertNotNull(resultado);
        assertEquals("Mensagem", resultado.mensagem());
        assertEquals("Solucao", resultado.solucao());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao inserir resposta com ID de usuário inválido")
    void inserirRespostaCenario2(){
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        InserirRespostaDTO inserirRespostaDTO = new InserirRespostaDTO(id, id, "Mensagem", "Solucao");

        assertThrows(EntityNotFoundException.class, () -> {
            respostaService.inserirResposta(inserirRespostaDTO);
        });
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao inserir resposta com ID de tópico inválido")
    void inserirRespostaCenario3(){
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(topicoRepository.findById(id)).thenReturn(Optional.empty());

        InserirRespostaDTO inserirRespostaDTO = new InserirRespostaDTO(id, id, "Mensagem", "Solucao");

        assertThrows(EntityNotFoundException.class, () -> {
            respostaService.inserirResposta(inserirRespostaDTO);
        });
    }

    @Test
    @DisplayName("Deve retornar DadosListagemRespostaDTO para resposta ativa com ID válido")
    void buscarPorIdCenario1(){
        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(id);

        Topico topico = new Topico();
        topico.setId(id);

        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setAtivo(true);
        resposta.setAutor(usuario);
        resposta.setTopico(topico);

        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.of(resposta));

        DadosListagemRespostaDTO dto = respostaService.buscarPorId(id);

        assertNotNull(dto);
        assertEquals(id, dto.id());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando não encontrar resposta com ID fornecido")
    void buscarPorIdCenario2(){
        Long id = 1L;
        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            respostaService.buscarPorId(id);
        });
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao buscar resposta com status inativo")
    void buscarPorIdCenario3(){
        Long id = 1L;
        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setAtivo(false);

        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.of(resposta));

        assertThrows(EntityNotFoundException.class, () -> {
            respostaService.buscarPorId(id);
        });
    }

    @Test
    @DisplayName("Deve atualizar uma resposta existente quando parâmetros forem válidos")
    void atualizarRespostaCenario1(){
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        Topico topico = new Topico();
        topico.setId(id);

        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setAtivo(true);
        resposta.setAutor(usuario);
        resposta.setTopico(topico);

        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.of(resposta));

        AtualizarRespostaDTO atualizarRespostaDTO = new AtualizarRespostaDTO(id, "Nova mensagem", "Nova solucao");

        DadosListagemRespostaDTO resultado = respostaService.atualizarResposta(atualizarRespostaDTO);

        Assertions.assertEquals("Nova mensagem", resultado.mensagem());
        Assertions.assertEquals("Nova solucao", resultado.solucao());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar uma resposta inativa")
    void atualizarRespostaCenario2(){
        Long id = 1L;

        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setAtivo(false);

        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.of(resposta));

        AtualizarRespostaDTO atualizarRespostaDTO = new AtualizarRespostaDTO(id, "Nova mensagem", "Nova solucao");

        assertThrows(EntityNotFoundException.class, () -> {
            respostaService.atualizarResposta(atualizarRespostaDTO);
        });
    }

    @Test
    @DisplayName("Deve desativar uma resposta quando o ID for válido")
    void excluirRespostaCenario1(){
        Long id = 1L;

        Resposta resposta = new Resposta();
        resposta.setId(id);
        resposta.setAtivo(true);

        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.of(resposta));

        respostaService.excluirResposta(id);

        assertFalse(resposta.isAtivo());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar excluir resposta com ID inexistente")
    void excluirRespostaCenario2(){
        Long id = 999L;

        Mockito.when(respostaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            respostaService.excluirResposta(id);
        });
    }
}