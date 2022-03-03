package com.dio.mercado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MercadoDTO {

    private long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @NotNull
    @Max(9999)
    private int quantidade;

    @NotNull
    @Max(9999)
    private int max = 9999;

    @NotNull
    private double preco;

}
