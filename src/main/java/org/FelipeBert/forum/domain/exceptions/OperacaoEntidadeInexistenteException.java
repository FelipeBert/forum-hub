package org.FelipeBert.forum.domain.exceptions;

public class OperacaoEntidadeInexistenteException extends RuntimeException{
    public OperacaoEntidadeInexistenteException(){
        super("Não é possível realizar a operação, pois a entidade especificada não existe no sistema.");
    }
}
