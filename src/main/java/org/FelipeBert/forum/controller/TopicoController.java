package org.FelipeBert.forum.controller;

import jakarta.validation.Valid;
import org.FelipeBert.forum.domain.dto.in.BuscaAnoDTO;
import org.FelipeBert.forum.domain.dto.in.CriarNovoTopicoDTO;
import org.FelipeBert.forum.domain.dto.out.DadosListagemTopicosDTO;
import org.FelipeBert.forum.infra.service.TopicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopicosDTO>> listarTopicos(@PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao){
        var topicos = topicoService.listarTopicos(paginacao);

        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<DadosListagemTopicosDTO>> listarTopicosPorCurso(@PathVariable Long id, @PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao){
        var topicos = topicoService.listarTopicosPorCurso(id, paginacao);

        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/ano")
    public ResponseEntity<Page<DadosListagemTopicosDTO>> listarTopicosPorAno(@RequestBody @Valid BuscaAnoDTO ano,
                                                                             @PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao){
        var topicos = topicoService.listarTopicosPorAno(ano, paginacao);

        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/criacao")
    public ResponseEntity<List<DadosListagemTopicosDTO>> listarTopicosPorCriacao(@PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao){
        var topicos = topicoService.listarDezTopicosPorDataCriacao(paginacao);

        return ResponseEntity.ok(topicos);
    }
}
