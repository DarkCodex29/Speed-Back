package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.DashboardDao;
import com.hochschild.speed.back.model.bean.DashboardBean;
import com.hochschild.speed.back.model.bean.dashboard.ModeloDocumentoBean;
import com.hochschild.speed.back.service.DashboardService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("DashboardService")
public class DashboardServiceImpl implements DashboardService {
    private static final Logger LOGGER = Logger.getLogger(DashboardServiceImpl.class.getName());
    private final DashboardDao dashboardDao;

    @Autowired
    public DashboardServiceImpl(DashboardDao dashboardDao) {
        this.dashboardDao = dashboardDao;
    }

    @Override
    public ResponseEntity<DashboardBean> validarAcceso(String usuario) {
        DashboardBean result = null;
        try {
            result = dashboardDao.validarAcceso(usuario);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ModeloDocumentoBean>> obtenerModeloDocumentos() {
        List<ModeloDocumentoBean> documentos = new ArrayList<>();
        try{
            documentos = dashboardDao.obtenerModeloDocumentos();
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(documentos, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
