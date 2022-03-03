package com.dio.mercado.servico.utilidades;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConversao {

    public static String asJsonString(Object mercadoDTO){
        try {
            ObjectMapper obj = new ObjectMapper();

            obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            obj.registerModule(new JavaTimeModule());
            return obj.writeValueAsString(mercadoDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
