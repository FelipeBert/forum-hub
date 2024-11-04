package org.FelipeBert.forum.controller;

import jakarta.validation.Valid;
import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.infra.service.TopicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    public ResponseEntity criarNovoTopico(@RequestBody @Valid CriarNovoTopicoDTO dados){
        topicoService.criarNovoTopico(dados);

        return ResponseEntity.ok().build();
    }
}
