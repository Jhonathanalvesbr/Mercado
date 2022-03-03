package com.dio.mercado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MercadoLimiteIncrimento extends Exception {

    public MercadoLimiteIncrimento(Long id, int quantityToIncrement) {
        super(String.format("Mercadoria de ID: %s excedeu a capacidade maxima, não é possível incriemtnar +%s.", id, quantityToIncrement));
    }
}