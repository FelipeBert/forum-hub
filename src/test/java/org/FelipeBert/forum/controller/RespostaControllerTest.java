package org.FelipeBert.forum.controller;

import org.FelipeBert.forum.domain.dto.in.AtualizarRespostaDTO;
import org.FelipeBert.forum.domain.dto.in.InserirRespostaDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemRespostaDTO;
import org.FelipeBert.forum.domain.service.RespostaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RespostaControllerTest {

    private RespostaService respostaService;
    private RespostaController respostaController;

    @BeforeEach
    void setUp(){
        respostaService = mock(RespostaService.class);
        respostaController = new RespostaController(respostaService);
    }

    @Test
    @DisplayName("Deve inserir nova resposta e retornar status 201 ao fornecer dados válidos")
    void inserirRespostaCenario1(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        InserirRespostaDTO dados = new InserirRespostaDTO(1L, 1L, "Mensagem", "Solucao");
        DadosListagemRespostaDTO respostaDTO = new DadosListagemRespostaDTO(1L, 1L, 1L, "Mensagem",
                "Solucao", LocalDateTime.now());

        when(respostaService.inserirResposta(dados)).thenReturn(respostaDTO);

        ResponseEntity responseEntity = respostaController.inserirResposta(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(respostaDTO, responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve retornar resposta com status 200 ao buscar por ID existente")
    void buscarRepostaPorIdCenario1(){
        Long validId = 1L;
        DadosListagemRespostaDTO respostaDTO = new DadosListagemRespostaDTO(1L, 1L, 1L, "Mensagem",
                "Solucao", LocalDateTime.now());

        when(respostaService.buscarPorId(validId)).thenReturn(respostaDTO);

        ResponseEntity responseEntity = respostaController.buscarRepostaPorId(validId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(respostaDTO, responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve atualizar resposta e retornar status 200 ao fornecer dados válidos")
    void atualizarRepostaCenario1(){
        AtualizarRespostaDTO dados = new AtualizarRespostaDTO(1L, "Mensagem Atualizada", "Solucao Atualizada");

        DadosListagemRespostaDTO respostaDTO = new DadosListagemRespostaDTO(1L, 1L, 1L, "Mensagem",
                "Solucao", LocalDateTime.now());

        when(respostaService.atualizarResposta(dados)).thenReturn(respostaDTO);

        ResponseEntity responseEntity = respostaController.atualizarReposta(dados);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(respostaDTO, responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve excluir resposta e retornar status 204 ao fornecer ID válido")
    void deletarRespostaCenario1(){
        Long validId = 1L;

        doNothing().when(respostaService).excluirResposta(validId);

        ResponseEntity response = respostaController.deletarResposta(validId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(respostaService, times(1)).excluirResposta(validId);
    }
}