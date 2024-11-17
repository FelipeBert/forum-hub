package org.FelipeBert.forum.domain.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CadastroUsuarioExistenteException.class)
    public ResponseEntity<String> handleCadastroUsuarioExistenteException(CadastroUsuarioExistenteException ex){
        return ResponseEntity.badRequest().body("O e-mail fornecido já está associado a uma conta existente. Por favor, utilize um e-mail diferente");
    }

    @ExceptionHandler(EntidadeInativaException.class)
    public ResponseEntity<String> handleEntidadeInativaException(EntidadeInativaException ex){
        return ResponseEntity.badRequest().body("Não é possível realizar a operação solicitada porque a entidade está marcada como inativa.");
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<String> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex){
        return ResponseEntity.badRequest().body("A entidade não foi encontrada no sistema. Verifique se o identificador está correto.");
    }

    @ExceptionHandler(ParametrosAtualizacaoInvalidosException.class)
    public ResponseEntity<String> handleParametrosAtualizacaoInvalidosException(ParametrosAtualizacaoInvalidosException ex){
        return ResponseEntity.badRequest()
                .body("Os parâmetros fornecidos para a atualização do tópico são inválidos ou estão ausentes. Certifique-se de que todos os campos obrigatórios estão preenchidos.");
    }

    @ExceptionHandler(SenhaNaoCorrespondeException.class)
    public ResponseEntity<String> handleSenhaNaoCorrespondeException(SenhaNaoCorrespondeException ex){
        return ResponseEntity.badRequest().body("A senha fornecida não corresponde à senha cadastrada. Por favor, tente novamente com a senha correta.");
    }

    @ExceptionHandler(OperacaoEntidadeInexistenteException.class)
    public ResponseEntity<String> handleOperacaoEntidadeInexistenteException(OperacaoEntidadeInexistenteException ex){
        return ResponseEntity.badRequest().body("Não é possível realizar a operação, pois a entidade especificada não existe no sistema.");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex){
        return ResponseEntity.badRequest().body("Não é possível realizar a operação, pois o parametro passado foi null.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body("Não é possível realizar a operação, pois o parametro passado foi Invalido.");
    }
}