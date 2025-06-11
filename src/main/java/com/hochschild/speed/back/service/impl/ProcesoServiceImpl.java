package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.filter.ProcesoFilter;
import com.hochschild.speed.back.repository.speed.ProcesoRepository;
import com.hochschild.speed.back.service.ProcesoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcesoServiceImpl implements ProcesoService {

    private static final Logger LOGGER = Logger.getLogger(ProcesoServiceImpl.class.getName());
    private final ProcesoRepository procesoRepository;

    @Autowired
    public ProcesoServiceImpl(ProcesoRepository procesoRepository) {
        this.procesoRepository = procesoRepository;
    }

    @Override
    public List<Proceso> find(ProcesoFilter filter) {

        List<Proceso> result;
        try {
            result = procesoRepository.findAll();

            if (filter.getTipoProceso() != null) {
                result = procesoRepository.findByIdTipoProceso(filter.getTipoProceso());
            }

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public Proceso findById(Integer idProceso) {

        Proceso proceso = new Proceso();

        try {
            proceso = procesoRepository.findOne(idProceso);

        } catch (Exception ex) {
            proceso = new Proceso();
            LOGGER.info(ex.getMessage(), ex);
        }
        return proceso;
    }
}