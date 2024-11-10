package org.FelipeBert.forum.controller;

import jakarta.validation.Valid;
import org.FelipeBert.forum.domain.dto.in.DadosAutenticacaoDTO;
import org.FelipeBert.forum.domain.model.Usuario;
import org.FelipeBert.forum.infra.security.DadosTokenDTO;
import org.FelipeBert.forum.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacaoDTO dados){
        var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(token);

        var tokenJwt = tokenService.generateToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenDTO(tokenJwt));
    }
}
