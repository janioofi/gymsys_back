package br.janioofi.msgym.exceptions;

public class RecordNotFoundException extends RuntimeException{
    public RecordNotFoundException(String msg){
        super(msg);
    }
}
