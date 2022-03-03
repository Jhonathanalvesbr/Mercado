package com.dio.mercado.mapear;

import com.dio.mercado.dto.MercadoDTO;
import com.dio.mercado.entidade.Mercado;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MercadoMapear {

    MercadoMapear INSTANCE = Mappers.getMapper(MercadoMapear.class);

    Mercado toModel(MercadoDTO beerDTO);

    MercadoDTO toDTO(Mercado beer);
}
