package org.FelipeBert.forum.controller;

import jakarta.validation.Valid;
import org.FelipeBert.forum.domain.dto.in.AtualizarSenhaDTO;
import org.FelipeBert.forum.domain.dto.in.CadastrarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.in.DeletarUsuarioDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemUsuarioDTO;
import org.FelipeBert.forum.domain.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemUsuarioDTO> buscarUsuario(@PathVariable Long id){
        var usuario = usuarioService.buscarUsuario(id);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid CadastrarUsuarioDTO dados, UriComponentsBuilder uriBuilder){
        var usuario = usuarioService.cadastrarUsuario(dados);

        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.id()).toUri();

        return ResponseEntity.created(uri).body(usuario);
    }

    @PutMapping
    public ResponseEntity atualizarSenha(@RequestBody @Valid AtualizarSenhaDTO dados){
        usuarioService.atualizarSenha(dados);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deletarUsuario(@RequestBody @Valid DeletarUsuarioDTO dados){
        usuarioService.deletarUsuario(dados);

        return ResponseEntity.noContent().build();
    }
}
