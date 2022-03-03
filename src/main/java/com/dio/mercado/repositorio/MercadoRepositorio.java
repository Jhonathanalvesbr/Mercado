package com.dio.mercado.repositorio;

import com.dio.mercado.entidade.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MercadoRepositorio  extends JpaRepository<Mercado, Long> {

    Optional<Mercado> findByName(String name);

}
