package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.HcArea;
import com.hochschild.speed.back.model.filter.HcAreaFilter;
import com.hochschild.speed.back.repository.speed.HcAreaRepository;
import com.hochschild.speed.back.service.HcAreaService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HcAreaServiceImpl implements HcAreaService {

    private static final Logger LOGGER = Logger.getLogger(HcAreaServiceImpl.class.getName());
    private final HcAreaRepository hcAreaRepository;

    @Autowired
    public HcAreaServiceImpl(HcAreaRepository hcAreaRepository) {
        this.hcAreaRepository = hcAreaRepository;
    }

    @Override
    public List<HcArea> find(HcAreaFilter filter) {

        List<HcArea> result;

        try {

            if (filter.getIdCompania() != null) {
                result = hcAreaRepository.listarAreasPorCompania(filter.getIdCompania(), Constantes.ESTADO_ACTIVO);
            } else {
                result = hcAreaRepository.listarAreasActivas();
            }

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }
}