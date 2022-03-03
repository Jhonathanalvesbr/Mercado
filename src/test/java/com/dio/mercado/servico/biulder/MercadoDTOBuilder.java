package com.dio.mercado.servico.biulder;

import com.dio.mercado.dto.MercadoDTO;
import lombok.Builder;

@Builder
public class MercadoDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String name = "Mineiro";

    @Builder.Default
    private int quantidade = 1;

    @Builder.Default
    private int max = 9999;

    @Builder.Default
    private double preco = 30.30;

   public MercadoDTO toMercadoDTO(){
       return new MercadoDTO(id,name,quantidade,max,preco);
   }
}
