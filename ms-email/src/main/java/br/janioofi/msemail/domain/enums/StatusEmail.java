package br.janioofi.msemail.domain.enums;

public enum StatusEmail {
    ENVIADO("Enviado"),
    ERROR("Error");

    private final String status;

    StatusEmail(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}