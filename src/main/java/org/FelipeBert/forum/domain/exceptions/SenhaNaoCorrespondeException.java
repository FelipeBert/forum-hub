package org.FelipeBert.forum.domain.exceptions;

public class SenhaNaoCorrespondeException extends RuntimeException{
    public SenhaNaoCorrespondeException(){
        super("A senha fornecida não corresponde à senha cadastrada. Por favor, tente novamente com a senha correta.");
    }
}
