package com.dio.mercado.controle;

import com.dio.mercado.dto.MercadoDTO;
import com.dio.mercado.dto.QuantidadeDTO;
import com.dio.mercado.entidade.Mercado;
import com.dio.mercado.exception.MercadoExisteRegistroException;
import com.dio.mercado.exception.MercadoLimiteDecremento;
import com.dio.mercado.exception.MercadoLimiteIncrimento;
import com.dio.mercado.exception.MercadoNaoAchouException;
import com.dio.mercado.servico.MercadoServico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mercado")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MercadoControle implements MercadoControleDoc{

    private final MercadoServico mercadoServico;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MercadoDTO criarMercado(@RequestBody @Valid MercadoDTO mercadoDTO) throws MercadoExisteRegistroException {
        return mercadoServico.criarMercado(mercadoDTO);
    }

    @GetMapping("/{name}")
    public MercadoDTO findByName(@PathVariable String name) throws MercadoNaoAchouException {
        return mercadoServico.findByName(name);
    }

    @GetMapping
    public List<MercadoDTO> listaMercado(){
        return mercadoServico.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) throws MercadoNaoAchouException {
        mercadoServico.deletarId(id);
    }

    @PatchMapping("/{id}/incremento")
    public MercadoDTO incremento(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) throws MercadoNaoAchouException, MercadoLimiteIncrimento {
        return mercadoServico.incremento(id, quantidadeDTO.getQuantidade());
    }

    @PatchMapping("/{id}/decremento")
    public MercadoDTO decremento(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) throws MercadoNaoAchouException, MercadoLimiteDecremento, MercadoLimiteIncrimento {
        return mercadoServico.decremento(id, quantidadeDTO.getQuantidade());
    }
}
