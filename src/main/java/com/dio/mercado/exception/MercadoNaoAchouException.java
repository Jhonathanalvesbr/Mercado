package com.dio.mercado.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MercadoNaoAchouException extends Exception{
    public MercadoNaoAchouException(String name){
        super(String.format("Mercadoria %s não encontrada!", name));
    }

    public MercadoNaoAchouException(Long id){
        super(String.format("mercadoria de id: %s não encontradd!", id));
    }
}
