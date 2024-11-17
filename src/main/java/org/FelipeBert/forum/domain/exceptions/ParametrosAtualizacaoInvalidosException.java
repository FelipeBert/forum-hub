package org.FelipeBert.forum.domain.exceptions;

public class ParametrosAtualizacaoInvalidosException extends RuntimeException{
    public ParametrosAtualizacaoInvalidosException(){
        super("Os parâmetros fornecidos para a atualização do tópico são inválidos ou estão ausentes. Certifique-se de que todos os campos obrigatórios estão preenchidos.");
    }
}
