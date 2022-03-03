package com.dio.mercado.servico;

import com.dio.mercado.dto.MercadoDTO;
import com.dio.mercado.entidade.Mercado;
import com.dio.mercado.exception.MercadoExisteRegistroException;
import com.dio.mercado.exception.MercadoLimiteDecremento;
import com.dio.mercado.exception.MercadoLimiteIncrimento;
import com.dio.mercado.exception.MercadoNaoAchouException;
import com.dio.mercado.mapear.MercadoMapear;
import com.dio.mercado.repositorio.MercadoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MercadoServico {

    private final MercadoRepositorio mercadoRepositorio;
    private final MercadoMapear mercadoMapear = MercadoMapear.INSTANCE;

    public MercadoDTO criarMercado(MercadoDTO mercadoDTO) throws MercadoExisteRegistroException {
        verificarnameExiste(mercadoDTO.getName());
        Mercado mercado = mercadoMapear.toModel(mercadoDTO);
        Mercado salvarMercado = mercadoRepositorio.save(mercado);
        return mercadoMapear.toDTO(salvarMercado);
    }

    public MercadoDTO findByName(String name) throws MercadoNaoAchouException {
        Mercado achouProduto = mercadoRepositorio.findByName(name)
                .orElseThrow(() -> new MercadoNaoAchouException(name));

        return mercadoMapear.toDTO(achouProduto);
    }

    public List<MercadoDTO> listAll(){
        return mercadoRepositorio.findAll()
                .stream()
                .map(mercadoMapear::toDTO)
                .collect(Collectors.toList());
    }

    public void deletarId(Long id) throws MercadoNaoAchouException {
        verificarExiste(id);
        mercadoRepositorio.deleteById(id);
    }

    public void verificarnameExiste(String name) throws MercadoExisteRegistroException {
        Optional<Mercado> optSalveMercado = mercadoRepositorio.findByName(name);
        if(optSalveMercado.isPresent()){
            throw new MercadoExisteRegistroException(name);

        }
    }

    public Mercado verificarExiste(Long id) throws MercadoNaoAchouException {
        return mercadoRepositorio.findById(id)
                .orElseThrow(() -> new MercadoNaoAchouException(id));
    }

    public MercadoDTO incremento(long id, int quantidadeIncremento) throws MercadoNaoAchouException, MercadoLimiteIncrimento {
        Mercado achou = verificarExiste(id);

        if(quantidadeIncremento+achou.getQuantidade() <= achou.getMax()){
            achou.setQuantidade(achou.getQuantidade()+quantidadeIncremento);
            Mercado incremento = mercadoRepositorio.save(achou);
            return mercadoMapear.toDTO(incremento);
        }

        throw new MercadoLimiteIncrimento(id,quantidadeIncremento);
    }

    public MercadoDTO decremento(Long id, Integer quantidade) throws MercadoNaoAchouException, MercadoLimiteIncrimento, MercadoLimiteDecremento {
        Mercado achou = verificarExiste(id);

        if(quantidade+achou.getQuantidade() >= 1){
            achou.setQuantidade(achou.getQuantidade()-quantidade);
            Mercado incremento = mercadoRepositorio.save(achou);
            return mercadoMapear.toDTO(incremento);
        }

        throw new MercadoLimiteDecremento(id,quantidade);

    }
}
