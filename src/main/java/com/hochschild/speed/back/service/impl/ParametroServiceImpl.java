package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.filter.ParametroFilter;
import com.hochschild.speed.back.repository.speed.BotonRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.service.ParametroService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParametroServiceImpl implements ParametroService {

    private static final Logger LOGGER = Logger.getLogger(ParametroServiceImpl.class.getName());
    private final ParametroRepository parametroRepository;
    private final BotonRepository botonRepository;

    @Autowired
    public ParametroServiceImpl(ParametroRepository parametroRepository,
                                BotonRepository botonRepository) {
        this.parametroRepository = parametroRepository;
        this.botonRepository = botonRepository;
    }

    @Override
    public List<Parametro> find(ParametroFilter filter) {

        List<Parametro> result;

        try {

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("tipo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            Parametro parametro = Parametro.builder().descripcion(filter.getDescripcion()).tipo(filter.getTipo()).build();

            result = parametroRepository.findAll(Example.of(parametro, matcher));

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public Parametro buscarPorId(Integer idParametro) {
        return parametroRepository.findById(idParametro);
    }

    @Override
    public List<Parametro> buscarParametroPorTipo(String tipo) {
        return parametroRepository.obtenerPorTipo(tipo);
    }

    @Override
    public Parametro obtenerPorTipoValor(String tipo, String valor) {
        return parametroRepository.obtenerPorTipoValor(tipo,valor);
    }

}