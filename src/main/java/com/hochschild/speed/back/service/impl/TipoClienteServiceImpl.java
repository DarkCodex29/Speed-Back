package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import com.hochschild.speed.back.model.filter.TipoClienteFilter;
import com.hochschild.speed.back.repository.speed.TipoClienteRepository;
import com.hochschild.speed.back.service.TipoClienteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TipoClienteServiceImpl implements TipoClienteService {

    private static final Logger LOGGER = Logger.getLogger(TipoClienteServiceImpl.class.getName());
    private final TipoClienteRepository tipoClienteRepository;

    @Autowired
    public TipoClienteServiceImpl(TipoClienteRepository tipoClienteRepository) {
        this.tipoClienteRepository = tipoClienteRepository;
    }

    @Override
    public List<TipoCliente> find(TipoClienteFilter filter) {

        List<TipoCliente> result;

        try {

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("codigo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("esRepresentateLegal", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            TipoCliente tipoCliente = TipoCliente.builder().codigo(filter.getCodigo()).verRepresentante(filter.getEsRepresentateLegal()).estado("S").build();

            result = tipoClienteRepository.findAll(Example.of(tipoCliente, matcher));

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }
}