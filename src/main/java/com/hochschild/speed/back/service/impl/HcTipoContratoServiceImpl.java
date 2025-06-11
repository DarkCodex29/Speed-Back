package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.repository.speed.HcTipoContratoRepository;
import com.hochschild.speed.back.service.HcTipoContratoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HcTipoContratoServiceImpl implements HcTipoContratoService {

    private static final Logger LOGGER = Logger.getLogger(HcPaisServiceImpl.class.getName());

    private final HcTipoContratoRepository hcTipoContratoRepository;

    @Autowired
    public HcTipoContratoServiceImpl(HcTipoContratoRepository hcTipoContratoRepository) {
        this.hcTipoContratoRepository = hcTipoContratoRepository;
    }

    @Override
    public ResponseEntity<List<HcTipoContrato>> list() {

        List<HcTipoContrato> result = null;
        try {
            result = hcTipoContratoRepository.getListaTipoContrato();
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


}
