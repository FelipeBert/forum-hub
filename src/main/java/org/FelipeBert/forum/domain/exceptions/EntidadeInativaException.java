package org.FelipeBert.forum.domain.exceptions;

public class EntidadeInativaException extends RuntimeException{
    public EntidadeInativaException(){
        super("Não é possível realizar a operação solicitada porque a entidade está marcada como inativa.");
    }
}
