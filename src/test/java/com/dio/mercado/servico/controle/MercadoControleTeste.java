package com.dio.mercado.servico.controle;

import com.dio.mercado.controle.MercadoControle;
import com.dio.mercado.dto.MercadoDTO;
import com.dio.mercado.dto.QuantidadeDTO;
import com.dio.mercado.exception.MercadoExisteRegistroException;
import com.dio.mercado.exception.MercadoNaoAchouException;
import com.dio.mercado.servico.MercadoServico;
import com.dio.mercado.servico.biulder.MercadoDTOBuilder;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.dio.mercado.servico.utilidades.JsonConversao.asJsonString;

@ExtendWith(MockitoExtension.class)
public class MercadoControleTeste {
    private static final String path = "/api/v1/mercado";
    private static final long idValido = 1L;
    private static final long idInvalido = 2L;
    private static final String mercadoIncremento = "/incremento";
    private static final String mercadoDecremento = "/decremento";

    private MockMvc mockMvc;

    @Mock
    private MercadoServico mercadoServico;
    @InjectMocks
    private MercadoControle mercadoControle;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mercadoControle)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void whenPOSTIsCalledThenAMercadoIsCreated() throws MercadoExisteRegistroException, Exception {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        Mockito.when(mercadoServico.criarMercado(mercadoDTO)).thenReturn(mercadoDTO);


        mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mercadoDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(mercadoDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantidade", Is.is(mercadoDTO.getQuantidade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco", Is.is(mercadoDTO.getPreco())));

    }

    @Test
    public void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws MercadoExisteRegistroException, Exception {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        mercadoDTO.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mercadoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        Mockito.when(mercadoServico.findByName(mercadoDTO.getName())).thenReturn(mercadoDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(path + "/" + mercadoDTO.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(mercadoDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantidade", Is.is(mercadoDTO.getQuantidade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco", Is.is(mercadoDTO.getPreco())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {

        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();


        Mockito.when(mercadoServico.findByName(mercadoDTO.getName())).thenThrow(MercadoNaoAchouException.class);


        mockMvc.perform(MockMvcRequestBuilders.get(path + "/" + mercadoDTO.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenGETListWithMercadoIsCalledThenOkStatusIsReturned() throws Exception {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        Mockito.when(mercadoServico.listAll()).thenReturn(Collections.singletonList(mercadoDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Is.is(mercadoDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantidade", Is.is(mercadoDTO.getQuantidade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].preco", Is.is(mercadoDTO.getPreco())));
    }

    @Test
    void whenGETListWithoutMercadoIsCalledThenOkStatusIsReturned() throws Exception {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        Mockito.when(mercadoServico.listAll()).thenReturn(Collections.singletonList(mercadoDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        Mockito.doNothing().when(mercadoServico).deletarId(mercadoDTO.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete(path+"/"+mercadoDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        Mockito.doThrow(MercadoNaoAchouException.class).when(mercadoServico).deletarId(idValido);

        mockMvc.perform(MockMvcRequestBuilders.delete(path+"/"+idValido)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder().quantidade(10).build();
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        mercadoDTO.setQuantidade(mercadoDTO.getQuantidade()+quantidadeDTO.getQuantidade());

        Mockito.when(mercadoServico.incremento(idValido, quantidadeDTO.getQuantidade())).thenReturn(mercadoDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(path+"/"+idValido+mercadoIncremento)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantidadeDTO))).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(mercadoDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantidade", Is.is(mercadoDTO.getQuantidade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco", Is.is(mercadoDTO.getPreco())));
    }

    @Test
    void whenPATCHIsCalledToDencrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder().quantidade(10).build();
        MercadoDTO mercadoDTO = MercadoDTOBuilder.builder().build().toMercadoDTO();

        mercadoDTO.setQuantidade(mercadoDTO.getQuantidade()-quantidadeDTO.getQuantidade());

        Mockito.when(mercadoServico.decremento(idValido, quantidadeDTO.getQuantidade())).thenReturn(mercadoDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(path+"/"+idValido+mercadoDecremento)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantidadeDTO))).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(mercadoDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantidade", Is.is(mercadoDTO.getQuantidade())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco", Is.is(mercadoDTO.getPreco())));
    }
}
