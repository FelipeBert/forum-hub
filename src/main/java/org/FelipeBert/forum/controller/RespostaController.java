package org.FelipeBert.forum.controller;

import jakarta.validation.Valid;
import org.FelipeBert.forum.domain.dto.in.AtualizarRespostaDTO;
import org.FelipeBert.forum.domain.dto.in.InserirRespostaDTO;
import org.FelipeBert.forum.domain.service.RespostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/resposta")
public class RespostaController {

    private RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @PostMapping
    public ResponseEntity inserirResposta(@RequestBody @Valid InserirRespostaDTO dados, UriComponentsBuilder uriBuilder){
        var resposta = respostaService.inserirResposta(dados);

        var uri = uriBuilder.path("/resposta/{id}").buildAndExpand(resposta.id()).toUri();

        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarRepostaPorId(@PathVariable Long id){
        var resposta = respostaService.buscarPorId(id);

        return ResponseEntity.ok(resposta);
    }

    @PutMapping
    public ResponseEntity atualizarReposta(@RequestBody @Valid AtualizarRespostaDTO dados){
        var resposta = respostaService.atualizarResposta(dados);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarResposta(@PathVariable Long id){
        respostaService.excluirResposta(id);

        return ResponseEntity.noContent().build();
    }
}
