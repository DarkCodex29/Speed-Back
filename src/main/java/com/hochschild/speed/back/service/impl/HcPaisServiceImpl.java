package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.HcPais;
import com.hochschild.speed.back.model.filter.HcPaisFilter;
import com.hochschild.speed.back.repository.speed.HcPaisRepository;
import com.hochschild.speed.back.service.HcPaisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HcPaisServiceImpl implements HcPaisService {

    private static final Logger LOGGER = Logger.getLogger(HcPaisServiceImpl.class.getName());
    private final HcPaisRepository hcPaisRepository;

    @Autowired
    public HcPaisServiceImpl(HcPaisRepository hcPaisRepository) {
        this.hcPaisRepository = hcPaisRepository;
    }

    @Override
    public List<HcPais> find(HcPaisFilter filter) {

        List<HcPais> result;

        try {

            ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("nombre",
                    ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

            HcPais hcPais = HcPais.builder().nombre(filter.getNombre()).estado("A").build();

            result = hcPaisRepository.findAll(Example.of(hcPais, matcher));

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }
}
