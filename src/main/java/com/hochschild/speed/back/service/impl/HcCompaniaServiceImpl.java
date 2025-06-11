package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.ItemComboBean;
import com.hochschild.speed.back.model.domain.speed.HcCompania;
import com.hochschild.speed.back.model.filter.HcCompaniaFilter;
import com.hochschild.speed.back.repository.speed.HcCompaniaRepository;
import com.hochschild.speed.back.service.HcCompaniaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HcCompaniaServiceImpl implements HcCompaniaService {

    private static final Logger LOGGER = Logger.getLogger(HcCompaniaServiceImpl.class.getName());
    private final HcCompaniaRepository hcCompaniaRepository;

    @Autowired
    public HcCompaniaServiceImpl(HcCompaniaRepository hcCompaniaRepository) {
        this.hcCompaniaRepository = hcCompaniaRepository;
    }

    @Override
    public List<HcCompania> find(HcCompaniaFilter filter) {

        List<HcCompania> result;
        try {

            if (filter.getIdPais() != null) {
                result = hcCompaniaRepository.getCompaniasActivasPorPais(filter.getIdPais());
            } else {
                result = hcCompaniaRepository.getCompaniasActivas();
            }

        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
        }
        return result;
    }


    @Override
    public ResponseEntity<List<ItemComboBean>> listarPorPais(HcCompaniaFilter filter) {

        List<ItemComboBean> result = null;
        try {
            result = hcCompaniaRepository.listarPorPais(filter.getIdPais());
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage(), ex);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
