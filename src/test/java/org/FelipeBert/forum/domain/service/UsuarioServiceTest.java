package org.FelipeBert.forum.domain.service;

import org.FelipeBert.forum.domain.dto.in.AtualizarSenhaDTO;
import org.FelipeBert.forum.domain.dto.in.CadastrarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.in.DeletarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemUsuarioDTO;
import org.FelipeBert.forum.domain.exceptions.*;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp(){
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso quando dados são válidos")
    void cadastrarUsuarioCenario1(){
        CadastrarUsuarioDTO dados = new CadastrarUsuarioDTO("John Doe", "john.doe@example.com", "password123");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(false);
        when(passwordEncoder.encode(dados.senha())).thenReturn("hashedPassword");

        DadosListagemUsuarioDTO result = usuarioService.cadastrarUsuario(dados);

        assertNotNull(result);
        assertEquals("John Doe", result.nome());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar CadastroUsuarioExistenteException ao cadastrar usuário com email já existente")
    void cadastrarUsuarioCenario2(){
        CadastrarUsuarioDTO dados = new CadastrarUsuarioDTO("John Doe", "john.doe@example.com", "password123");
        when(usuarioRepository.existsByEmail(dados.email())).thenReturn(true);


        assertThrows(CadastroUsuarioExistenteException.class, () -> {
            usuarioService.cadastrarUsuario(dados);
        });
    }

    @Test
    @DisplayName("Deve retornar DadosListagemUsuarioDTO ao buscar usuário existente por ID")
    void buscarUsuarioCenario1(){
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Nome");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        DadosListagemUsuarioDTO result = usuarioService.buscarUsuario(id);

        assertNotNull(result);
        assertEquals("Nome", result.nome());
    }

    @Test
    @DisplayName("Deve lançar EntidadeNaoEncontradaException ao buscar usuário inexistente por ID")
    void buscarUsuarioCenario2(){
        Long id = 999L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            usuarioService.buscarUsuario(id);
        });
    }

    @Test
    @DisplayName("Deve atualizar senha do usuário com sucesso quando dados são válidos")
    void atualizarSenhaCenario1(){
        String email = "test@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        AtualizarSenhaDTO dados = new AtualizarSenhaDTO(email, oldPassword, newPassword);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(oldPassword);
        usuario.setAtivo(true);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
        when(passwordEncoder.matches(oldPassword, usuario.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        usuarioService.atualizarSenha(dados);

        verify(usuarioRepository).save(usuario);
        assertEquals("encodedNewPassword", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lançar OperacaoEntidadeInexistenteException ao tentar atualizar senha com email não cadastrado")
    void atualizarSenhaCenario2(){
        String email = "test@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        AtualizarSenhaDTO dados = new AtualizarSenhaDTO(email, oldPassword, newPassword);

        when(usuarioRepository.existsByEmail(email)).thenReturn(false);

        assertThrows(OperacaoEntidadeInexistenteException.class, () -> {
            usuarioService.atualizarSenha(dados);
        });
    }

    @Test
    @DisplayName("Deve lançar EntidadeInativaException ao tentar atualizar senha de usuário inativo")
    void atualizarSenhaCenario3(){
        String email = "test@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        AtualizarSenhaDTO dados = new AtualizarSenhaDTO(email, oldPassword, newPassword);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(oldPassword);
        usuario.setAtivo(false);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);

        assertThrows(EntidadeInativaException.class, () -> {
            usuarioService.atualizarSenha(dados);
        });
    }

    @Test
    @DisplayName("Deve lançar SenhaNaoCorrespondeException ao tentar atualizar senha com senha antiga incorreta")
    void atualizarSenhaCenario4(){
        String email = "test@example.com";
        String oldPassword = "oldPasword";
        String newPassword = "newPassword";
        String currentPassword = "currentPassword";

        AtualizarSenhaDTO dados = new AtualizarSenhaDTO(email, oldPassword, newPassword);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(currentPassword);
        usuario.setAtivo(true);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
        when(passwordEncoder.matches(oldPassword, usuario.getPassword())).thenReturn(false);

        assertThrows(SenhaNaoCorrespondeException.class, () -> {
            usuarioService.atualizarSenha(dados);
        });
    }

    @Test
    @DisplayName("Deve desativar usuário com sucesso ao deletar usuário com dados válidos")
    void deletarUsuarioCenario1(){
        String email = "test@example.com";
        String password = "Password";

        DeletarUsuarioDTO dados = new DeletarUsuarioDTO(email, password);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(password);
        usuario.setAtivo(true);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
        when(passwordEncoder.matches(password, usuario.getPassword())).thenReturn(true);

        usuarioService.deletarUsuario(dados);

        assertFalse(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve lançar OperacaoEntidadeInexistenteException ao tentar deletar usuário com email não cadastrado")
    void deletarUsuarioCenario2(){
        String email = "test@example.com";
        String password = "Password";

        DeletarUsuarioDTO dados = new DeletarUsuarioDTO(email, password);

        when(usuarioRepository.existsByEmail(email)).thenReturn(false);

        assertThrows(OperacaoEntidadeInexistenteException.class, () -> {
            usuarioService.deletarUsuario(dados);
        });
    }

    @Test
    @DisplayName("Deve lançar EntidadeInativaException ao tentar deletar usuário já inativo")
    void deletarUsuarioCenario3(){
        String email = "test@example.com";
        String password = "Password";

        DeletarUsuarioDTO dados = new DeletarUsuarioDTO(email, password);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(password);
        usuario.setAtivo(false);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);

        assertThrows(EntidadeInativaException.class, () -> {
            usuarioService.deletarUsuario(dados);
        });
    }

    @Test
    @DisplayName("Deve lançar SenhaNaoCorrespondeException ao tentar deletar usuário com senha incorreta")
    void deletarUsuarioCenario4(){
        String email = "test@example.com";
        String wrongPassword = "wrongPassword";
        String password= "password";

        DeletarUsuarioDTO dados = new DeletarUsuarioDTO(email, wrongPassword);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(password);
        usuario.setAtivo(true);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
        when(passwordEncoder.matches(wrongPassword, usuario.getPassword())).thenReturn(false);

        assertThrows(SenhaNaoCorrespondeException.class, () -> {
            usuarioService.deletarUsuario(dados);
        });
    }

}