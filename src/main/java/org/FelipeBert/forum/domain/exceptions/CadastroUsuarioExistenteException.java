package org.FelipeBert.forum.domain.exceptions;

public class CadastroUsuarioExistenteException extends RuntimeException{

    public CadastroUsuarioExistenteException(){
        super("O e-mail fornecido já está associado a uma conta existente. Por favor, utilize um e-mail diferente.");
    }

}
