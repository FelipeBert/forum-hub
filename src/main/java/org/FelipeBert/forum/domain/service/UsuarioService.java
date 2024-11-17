package org.FelipeBert.forum.domain.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.FelipeBert.forum.domain.dto.in.AtualizarSenhaDTO;
import org.FelipeBert.forum.domain.dto.in.CadastrarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.in.DeletarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemUsuarioDTO;
import org.FelipeBert.forum.domain.exceptions.*;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.infra.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DadosListagemUsuarioDTO buscarUsuario(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario"));
        return new DadosListagemUsuarioDTO(usuario.getId(), usuario.getNome());
    }

    @Transactional
    public DadosListagemUsuarioDTO cadastrarUsuario(CadastrarUsuarioDTO dados){
        if(verificaUsuarioExistente(dados.email())){
            throw new CadastroUsuarioExistenteException();
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());

        var passwordHash = passwordEncoder.encode(dados.senha());

        usuario.setSenha(passwordHash);

        usuarioRepository.save(usuario);

        return new DadosListagemUsuarioDTO(usuario.getId(), usuario.getNome());
    }

    @Transactional
    public void atualizarSenha(AtualizarSenhaDTO dados) {
        if(!verificaUsuarioExistente(dados.email())){
            throw new OperacaoEntidadeInexistenteException();
        }
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(dados.email());

        if(!usuario.isAtivo()){
            throw new EntidadeInativaException();
        }

        if(!passwordEncoder.matches(dados.senhaAntiga(), usuario.getPassword())){
            throw new SenhaNaoCorrespondeException();
        }

        usuario.setSenha(passwordEncoder.encode(dados.senhaNova()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletarUsuario(DeletarUsuarioDTO dados) {
        if(!verificaUsuarioExistente(dados.email())){
            throw new OperacaoEntidadeInexistenteException();
        }

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(dados.email());

        if(!usuario.isAtivo()){
            throw new EntidadeInativaException();
        }

        if(!passwordEncoder.matches(dados.senha(), usuario.getPassword())){
            throw new SenhaNaoCorrespondeException();
        }

        usuario.setAtivo(false);
    }

    private boolean verificaUsuarioExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }
}
