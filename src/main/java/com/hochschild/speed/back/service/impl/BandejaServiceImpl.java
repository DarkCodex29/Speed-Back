package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.dao.BandejaEntradaDao;
import com.hochschild.speed.back.model.bean.BandejaBean;
import com.hochschild.speed.back.model.bean.MisPendientesBean;
import com.hochschild.speed.back.model.bean.SolicitudesPorEnviarBean;
import com.hochschild.speed.back.model.filter.bandejaEntrada.BandejaEntradaFilter;
import com.hochschild.speed.back.model.filter.bandejaMisSolicitudes.BandejaMisSolicitudesFilter;
import com.hochschild.speed.back.model.filter.bandejaPendientes.BandejaPendientesFilter;
import com.hochschild.speed.back.model.filter.bandejaSolicitudesPorEnviar.BandejaSolicitudesPorEnviarFilter;
import com.hochschild.speed.back.service.BandejaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("BandejaService")
public class BandejaServiceImpl implements BandejaService {

    private static final Logger LOGGER = Logger.getLogger(BandejaServiceImpl.class.getName());
    private final BandejaEntradaDao bandejaEntradaDao;

    @Autowired
    public BandejaServiceImpl(BandejaEntradaDao bandejaEntradaDao) {
        this.bandejaEntradaDao = bandejaEntradaDao;
    }

    @Override
    public ResponseEntity<List<BandejaBean>> obtenerBandejaEntrada(BandejaEntradaFilter bandejaEntradaFilter) {

        List<BandejaBean> result = null;

        try {
            result = bandejaEntradaDao.listadoBandejaEntrada(bandejaEntradaFilter);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<MisPendientesBean>> obtenerMisPendientes(BandejaPendientesFilter bandejaPendientesFilter) {

        List<MisPendientesBean> result = null;

        try {
            result = bandejaEntradaDao.listadoBandejaPendientes(bandejaPendientesFilter);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<MisPendientesBean>> obtenerMisSolicitudes(BandejaMisSolicitudesFilter bandejaMisSolicitudesFilter) {

        List<MisPendientesBean> result = null;

        try {
            result = bandejaEntradaDao.listadoBandejaMisSolicitudes(bandejaMisSolicitudesFilter);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<SolicitudesPorEnviarBean>> obtenerSolicitudesPorEnviar(BandejaSolicitudesPorEnviarFilter bandejaSolicitudesPorEnviarFilter) {

        List<SolicitudesPorEnviarBean> result = null;

        try {
            result = bandejaEntradaDao.listadoBandejaSolicitudesPorEnviar(bandejaSolicitudesPorEnviarFilter);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}