package com.dio.mercado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MercadoExisteRegistroException extends Throwable {
    public MercadoExisteRegistroException(String name) {
        super(String.format("Mercadoria %s já existe!", name));
    }
}
