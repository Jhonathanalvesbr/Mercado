package com.dio.mercado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MercadoLimiteDecremento extends Exception {

    public MercadoLimiteDecremento(Long id, int quantityToIncrement) {
        super(String.format("Mercadoria de ID: %s excedeu a capacidade mínima, não é possível decrementar +%s.", id, quantityToIncrement));
    }
}