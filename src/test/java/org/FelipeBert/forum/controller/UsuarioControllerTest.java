package org.FelipeBert.forum.controller;

import org.FelipeBert.forum.domain.dto.in.AtualizarSenhaDTO;
import org.FelipeBert.forum.domain.dto.in.CadastrarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.in.DeletarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemUsuarioDTO;
import org.FelipeBert.forum.domain.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UsuarioControllerTest {
    private UsuarioService usuarioService;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp(){
        usuarioService = mock(UsuarioService.class);
        usuarioController = new UsuarioController(usuarioService);
    }

    @Test
    @DisplayName("Deve retornar usuário com status 200 ao buscar por ID válid")
    void buscarUsuarioCenario1(){
        Long id = 1L;
        DadosListagemUsuarioDTO usuario = new DadosListagemUsuarioDTO(id, "John Doe");
        when(usuarioService.buscarUsuario(id)).thenReturn(usuario);

        ResponseEntity<DadosListagemUsuarioDTO> response = new UsuarioController(usuarioService).buscarUsuario(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    @DisplayName("Deve cadastrar usuário com status 201 ao enviar dados válidos")
    void cadastrarUsuarioCenario1(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        CadastrarUsuarioDTO dados = new CadastrarUsuarioDTO("John Doe", "john.doe@example.com", "password123");
        DadosListagemUsuarioDTO usuario = new DadosListagemUsuarioDTO(1L, "John Doe");

        when(usuarioService.cadastrarUsuario(dados)).thenReturn(usuario);

        ResponseEntity response = new UsuarioController(usuarioService).cadastrarUsuario(dados, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando os dados de entrada para criacao forem nulos")
    void cadastrarUsuarioCenario2(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        CadastrarUsuarioDTO dados = new CadastrarUsuarioDTO(null, null, null);

        assertThrows(NullPointerException.class, () -> {
            usuarioController.cadastrarUsuario(dados, uriBuilder);
        });
    }

    @Test
    @DisplayName("Deve atualizar a senha com status 200 ao fornecer dados corretos")
    void atualizarSenhaCenario1(){
        AtualizarSenhaDTO dados = new AtualizarSenhaDTO("john.doe@example.com", "oldPassword", "newPassword");

        doNothing().when(usuarioService).atualizarSenha(dados);

        ResponseEntity response = new UsuarioController(usuarioService).atualizarSenha(dados);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService, times(1)).atualizarSenha(dados);
    }

    @Test
    @DisplayName("Deve deletar usuário com status 204 ao fornecer credenciais corretas")
    void deletarUsuarioCenario1(){
        DeletarUsuarioDTO deletarUsuarioDTO = new DeletarUsuarioDTO("john.doe@example.com", "correctPassword");

        doNothing().when(usuarioService).deletarUsuario(deletarUsuarioDTO);

        ResponseEntity response = new UsuarioController(usuarioService).deletarUsuario(deletarUsuarioDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService, times(1)).deletarUsuario(deletarUsuarioDTO);
    }
}