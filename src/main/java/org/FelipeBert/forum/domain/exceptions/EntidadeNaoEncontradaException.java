package org.FelipeBert.forum.domain.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException{
    public EntidadeNaoEncontradaException(String entidade){
        super("A entidade " + entidade + " não foi encontrada no sistema. Verifique se o identificador está correto.");
    }
}
