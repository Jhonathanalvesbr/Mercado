package com.dio.mercado.servico;

import com.dio.mercado.dto.MercadoDTO;
import com.dio.mercado.entidade.Mercado;
import com.dio.mercado.mapear.MercadoMapear;
import com.dio.mercado.repositorio.MercadoRepositorio;
import com.dio.mercado.servico.biulder.MercadoDTOBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MercadoServicoTeste {

    @Mock
    private MercadoRepositorio mercadoRepositorio;

    private MercadoMapear mercadoMapear = MercadoMapear.INSTANCE;

    @InjectMocks
    private MercadoServico mercadoServico;

    @Test
    void whenMercadoInformedThenItShouldBeCreated(){
        MercadoDTO mercado = MercadoDTOBuilder.builder().build().toMercadoDTO();


    }
}
