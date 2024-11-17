package org.FelipeBert.forum.controller;

import org.FelipeBert.forum.domain.dto.in.AtualizarTopicoDTO;
import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemTopicosDTO;
import org.FelipeBert.forum.domain.exceptions.ParametrosAtualizacaoInvalidosException;
import org.FelipeBert.forum.domain.model.Curso;
import org.FelipeBert.forum.domain.model.Topico;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.domain.service.TopicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TopicoControllerTest {
    private TopicoService topicoService;
    private TopicoController topicoController;

    @BeforeEach
    void setUp(){
        topicoService = mock(TopicoService.class);
        topicoController = new TopicoController(topicoService);
    }

    @Test
    @DisplayName("Deve criar novo tópico com status 201 ao fornecer parâmetros válidos")
    void criarNovoTopicoCenario1(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        CriarNovoTopicoDTO dados = new CriarNovoTopicoDTO("Titulo", "Mensagem", 1L, 1L);

        Usuario usuario = new Usuario();
        usuario.setNome("Nome Usuario");

        Curso curso = new Curso();
        curso.setNome("Nome Curso");

        Topico topico = new Topico();
        topico.setId(1L);
        topico.setTitulo("Titulo");
        topico.setMensagem("Mensagem");
        topico.setAutor(usuario);
        topico.setCurso(curso);

        when(topicoService.criarNovoTopico(dados)).thenReturn(topico);

        ResponseEntity response = topicoController.criarNovoTopico(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando os dados de entrada para criacao forem nulos")
    void criarNovoTopicoCenario2(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        CriarNovoTopicoDTO dados = new CriarNovoTopicoDTO(null, null, null, null);

        assertThrows(NullPointerException.class, () -> {
            topicoController.criarNovoTopico(dados, uriBuilder);
        });
    }

    @Test
    @DisplayName("Deve atualizar tópico com status 200 ao fornecer parâmetros válidos")
    void atualizarTopicoCenario1(){
        Usuario usuario = new Usuario();
        usuario.setNome("Nome Usuario");

        Curso curso = new Curso();
        curso.setNome("Nome Curso");

        Topico topico = new Topico();
        topico.setId(1L);
        topico.setTitulo("Titulo");
        topico.setMensagem("Mensagem");
        topico.setAutor(usuario);
        topico.setCurso(curso);

        AtualizarTopicoDTO dados = new AtualizarTopicoDTO("New Title", "New Message");
        DadosListagemTopicosDTO expectedResponse = new DadosListagemTopicosDTO(topico);

        when(topicoService.atualizarTopico(1L, dados)).thenReturn(expectedResponse);

        ResponseEntity<DadosListagemTopicosDTO> response = topicoController.atualizarTopico(1L, dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    @DisplayName("Deve lançar ParametrosAtualizacaoInvalidosException quando os dados de entrada para atualizacao forem nulos")
    void atualizarTopicoCenario2(){
        AtualizarTopicoDTO dados = new AtualizarTopicoDTO(null, null);

        when(topicoService.atualizarTopico(1L, dados)).thenThrow(new ParametrosAtualizacaoInvalidosException());

        assertThrows(ParametrosAtualizacaoInvalidosException.class, () -> {
            topicoController.atualizarTopico(1L, dados);
        });
    }

    @Test
    @DisplayName("Deve retornar tópico com status 200 ao buscar por ID existente")
    void buscarTopicoPorIdCenario1(){
        Long id = 1L;

        DadosListagemTopicosDTO expectedTopico = new DadosListagemTopicosDTO("Title", "Message",
                LocalDateTime.now(), "Author", "Course");

        when(topicoService.buscarTopicoPorId(id)).thenReturn(expectedTopico);
        TopicoController controller = new TopicoController(topicoService);

        ResponseEntity<DadosListagemTopicosDTO> response = controller.buscarTopicoPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTopico, response.getBody());
    }

    @Test
    @DisplayName("Deve excluir tópico com status 204 ao fornecer ID válido")
    void deletarTopicoCenario1(){
        Long existingId = 1L;

        doNothing().when(topicoService).excluirTopico(existingId);

        ResponseEntity response = topicoController.deletarTopico(existingId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(topicoService, times(1)).excluirTopico(existingId);
    }
}