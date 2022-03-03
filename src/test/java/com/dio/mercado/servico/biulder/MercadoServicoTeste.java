package com.dio.mercado.servico.biulder;

import com.dio.mercado.dto.MercadoDTO;
import com.dio.mercado.entidade.Mercado;
import com.dio.mercado.exception.MercadoExisteRegistroException;
import com.dio.mercado.exception.MercadoLimiteIncrimento;
import com.dio.mercado.exception.MercadoNaoAchouException;
import com.dio.mercado.mapear.MercadoMapear;
import com.dio.mercado.repositorio.MercadoRepositorio;
import com.dio.mercado.servico.MercadoServico;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MercadoServicoTeste {
    private static final long id = 1L;

    @Mock
    private MercadoRepositorio mercadoRepositorio;

    private MercadoMapear mercadoMapear = MercadoMapear.INSTANCE;

    @InjectMocks
    private MercadoServico mercadoServico;

    @Test
    public void whenMercadoInformedThenItShouldBeCreated() throws MercadoExisteRegistroException {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercadoSalvar = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findByName(mercadoDTO.getName())).thenReturn(Optional.empty());
        Mockito.when(mercadoRepositorio.save(mercadoSalvar)).thenReturn(mercadoSalvar);

        MercadoDTO mercadoDTO1 = mercadoServico.criarMercado(mercadoDTO);

        MatcherAssert.assertThat(mercadoDTO.getId(), Matchers.is(Matchers.equalTo(mercadoDTO1.getId())));
        MatcherAssert.assertThat(mercadoDTO.getName(), Matchers.is(Matchers.equalTo(mercadoDTO1.getName())));
        MatcherAssert.assertThat(mercadoDTO.getQuantidade(), Matchers.is(Matchers.equalTo(mercadoDTO1.getQuantidade())));
        MatcherAssert.assertThat(mercadoDTO.getPreco(), Matchers.is(Matchers.equalTo(mercadoDTO1.getPreco())));

    }

    @Test
    public void whenAlreadRegisterInformedThenAnExceptionShouldBeThrown() {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercadoDuplicado = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findByName(mercadoDTO.getName())).thenReturn(Optional.of(mercadoDuplicado));

        assertThrows(MercadoExisteRegistroException.class, () -> mercadoServico.criarMercado(mercadoDTO));
    }

    @Test
    public void whenValidMercadoNameIsGivenThenReturnAMercado() throws MercadoNaoAchouException {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercadoDuplicado = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findByName(mercadoDuplicado.getName())).thenReturn(Optional.of(mercadoDuplicado));


        MercadoDTO encontrouMercado = mercadoServico.findByName(mercadoDuplicado.getName());

        MatcherAssert.assertThat(encontrouMercado, Is.is(mercadoDTO));
    }

    @Test
    public void whenNotRegisterMercadoNameIsGivenThenThrowAnException() {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        Mockito.when(mercadoRepositorio.findByName(mercadoDTO.getName())).thenReturn(Optional.empty());

        Assertions.assertThrows(MercadoNaoAchouException.class, () -> mercadoServico.findByName(mercadoDTO.getName()));
    }

    @Test
    public void whenListMercadoIsCalledThenReturnAListOfMercado() {

        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercadoAchou = mercadoMapear.toModel(mercadoDTO);


        Mockito.when(mercadoRepositorio.findAll()).thenReturn(Collections.singletonList(mercadoAchou));

        List <MercadoDTO> lista = mercadoServico.listAll();

        MatcherAssert.assertThat(lista, Is.is(Matchers.not(Matchers.empty())));
        MatcherAssert.assertThat(lista.get(0), Is.is(Matchers.equalTo(mercadoDTO)));

    }

    @Test
    public void whenListMercadoIsCalledThenReturnAEmptyListOfMercado() {
        Mockito.when(mercadoRepositorio.findAll()).thenReturn(Collections.EMPTY_LIST);

        List <MercadoDTO> lista = mercadoServico.listAll();

        MatcherAssert.assertThat(lista, Is.is(Matchers.empty()));

    }

    @Test
    public void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws MercadoNaoAchouException {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercado = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findById(mercadoDTO.getId())).thenReturn(Optional.of(mercado));

        Mockito.doNothing().when(mercadoRepositorio).deleteById(mercadoDTO.getId());
        mercadoServico.deletarId(mercadoDTO.getId());

        Mockito.verify(mercadoRepositorio, Mockito.times(1)).findById(mercadoDTO.getId());
        Mockito.verify(mercadoRepositorio, Mockito.times(1)).deleteById(mercadoDTO.getId());
    }

    @Test
    public void whenIncrementIsCalledThenIncrementBeerStock() throws MercadoNaoAchouException, MercadoLimiteIncrimento {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercado = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findById(mercadoDTO.getId())).thenReturn(Optional.of(mercado));
        Mockito.when(mercadoRepositorio.save(mercado)).thenReturn(mercado);

        int quantidadeIncremento = 1000;
        int quantidadeExperado = mercadoDTO.getQuantidade() + quantidadeIncremento;

        MercadoDTO incromentoDTO = mercadoServico.incremento(mercadoDTO.getId(), quantidadeIncremento);

        MatcherAssert.assertThat(quantidadeExperado, Matchers.equalTo(incromentoDTO.getQuantidade()));

        MatcherAssert.assertThat(quantidadeExperado, Matchers.lessThan(mercadoDTO.getMax()));

    }

    @Test
    public void whenIncrementIsGreatherThanMaxThenThrowException() {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercado = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findById(mercadoDTO.getId())).thenReturn(Optional.of(mercado));

        int quantidade = 10100;
        Assertions.assertThrows(MercadoLimiteIncrimento.class, () -> mercadoServico.incremento(mercadoDTO.getId(), quantidade));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();
        Mercado mercado = mercadoMapear.toModel(mercadoDTO);

        Mockito.when(mercadoRepositorio.findById(mercadoDTO.getId())).thenReturn(Optional.of(mercado));

        int quantidade = 10045;
        assertThrows(MercadoLimiteIncrimento.class, () -> mercadoServico.incremento(mercadoDTO.getId(), quantidade));
    }

}
