package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.HcUbicacionBean;
import com.hochschild.speed.back.model.domain.speed.HcCompania;
import com.hochschild.speed.back.model.domain.speed.HcTipoUbicacion;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcCompaniaRepository;
import com.hochschild.speed.back.repository.speed.HcTipoUbicacionRepository;
import com.hochschild.speed.back.repository.speed.HcUbicacionRepository;
import com.hochschild.speed.back.service.HcUbicacionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HcUbicacionServiceImpl implements HcUbicacionService {

    private static final Logger LOGGER = Logger.getLogger(HcUbicacionServiceImpl.class.getName());
    private final HcUbicacionRepository hcUbicacionRepository;
    private final HcTipoUbicacionRepository hcTipoUbicacionRepository;
    private final HcCompaniaRepository hcCompaniaRepository;

    @Autowired
    public HcUbicacionServiceImpl(HcUbicacionRepository hcUbicacionRepository,
                                  HcTipoUbicacionRepository hcTipoUbicacionRepository,
                                  HcCompaniaRepository hcCompaniaRepository) {
        this.hcUbicacionRepository = hcUbicacionRepository;
        this.hcTipoUbicacionRepository = hcTipoUbicacionRepository;
        this.hcCompaniaRepository = hcCompaniaRepository;
    }

    @Override
    public ResponseModel guardarHcUbicacion(HcUbicacionBean hcUbicacionBean) {

        ResponseModel responseModel = new ResponseModel();
        HcUbicacion hcUbicacion = new HcUbicacion();

        HcTipoUbicacion hcTipoUbicacion = hcTipoUbicacionRepository.findById(hcUbicacionBean.getIdTipoUbicacion());
        HcCompania hcCompania = hcCompaniaRepository.findById(hcUbicacionBean.getIdCompania());

        if (hcUbicacionBean.getIdUbicacion() == null) {

            hcUbicacion.setNombre(hcUbicacionBean.getNombre());
            hcUbicacion.setTipo_ubicacion(hcTipoUbicacion);
            hcUbicacion.setEstado(hcUbicacionBean.getEstado());
            hcUbicacion.setCompania(hcCompania);
            hcUbicacion.setCodigo(hcUbicacionBean.getCodigo());

            hcUbicacionRepository.save(hcUbicacion);

            responseModel.setId(hcUbicacion.getId());
            responseModel.setMessage("Ubicacion registrada");

        } else {

            HcUbicacion existente = hcUbicacionRepository.findById(hcUbicacionBean.getIdUbicacion());

            hcUbicacion.setNombre(hcUbicacionBean.getNombre());
            hcUbicacion.setTipo_ubicacion(hcTipoUbicacion);
            hcUbicacion.setEstado(hcUbicacionBean.getEstado());
            hcUbicacion.setCompania(hcCompania);
            hcUbicacion.setCodigo(hcUbicacionBean.getCodigo());

            hcUbicacionRepository.save(existente);

            responseModel.setId(existente.getId());
            responseModel.setMessage("Ubicación actualizada");
        }

        return responseModel;
    }


    @Override
    public ResponseEntity<List<HcTipoUbicacion>> obtenerHcTipoUbicacion() {

        List<HcTipoUbicacion> result = null;

        try {
            result = hcTipoUbicacionRepository.listarHcTipoUbicacion();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Override
    public ResponseEntity<List<HcUbicacion>> listarUbicaciones() {

        List<HcUbicacion> result = null;

        try {
            result = hcUbicacionRepository.listarUbicacionesActivas();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            result = new ArrayList<>();
            LOGGER.info(ex.getMessage(), ex);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}