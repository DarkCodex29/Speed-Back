package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.model.domain.speed.Alerta;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.filter.bandejaSolicitudesPorEnviar.BandejaSolicitudesPorEnviarFilter;
import com.hochschild.speed.back.model.filter.bandejaMisSolicitudes.BandejaMisSolicitudesFilter;
import com.hochschild.speed.back.model.filter.bandejaPendientes.BandejaPendientesFilter;
import com.hochschild.speed.back.model.filter.bandejaEntrada.BandejaEntradaFilter;
import com.hochschild.speed.back.model.bean.SolicitudesPorEnviarBean;
import com.hochschild.speed.back.repository.speed.TrazaRepository;
import com.hochschild.speed.back.service.AlertaService;
import com.hochschild.speed.back.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.hochschild.speed.back.model.bean.MisPendientesBean;
import com.hochschild.speed.back.service.EstadoService;
import com.hochschild.speed.back.model.bean.BandejaBean;
import com.hochschild.speed.back.dao.BandejaEntradaDao;
import com.hochschild.speed.back.dao.HcDocumentoLegalDao;

import org.springframework.stereotype.Repository;
import com.hochschild.speed.back.util.DateUtil;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BandejaEntradaDaoImpl implements BandejaEntradaDao {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(BandejaEntradaDaoImpl.class.getName());
    private final EstadoService estadoService;
    private final EntityManager entityManager;
    private final AlertaService alertaService;
    private final TrazaRepository trazaRepository;
    private final HcDocumentoLegalDao hcDocumentoLegalDao;

    @Autowired
    public BandejaEntradaDaoImpl(EstadoService estadoService,
                                 @Qualifier("speedEntityManagerFactory") EntityManager entityManager,
                                 AlertaService alertaService,
                                 TrazaRepository trazaRepository,
                                 HcDocumentoLegalDao hcDocumentoLegalDao) {
        this.estadoService = estadoService;
        this.entityManager = entityManager;
        this.alertaService = alertaService;
        this.trazaRepository = trazaRepository;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BandejaBean> listadoBandejaEntrada(BandejaEntradaFilter bandejaEntradaBean) {

        List<BandejaBean> listadoBandejaEntrada = new ArrayList<>();

        try {
            LOGGER.info(bandejaEntradaBean);
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("stpr_Listado_BandejaEntrada");
            Date fecha = DateUtil.convertStringToDate(bandejaEntradaBean.getFecha(), DateUtil.FORMAT_DATE_XML);

            // Register inputs
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(8, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);

            // Set parameters
            query.setParameter(1, bandejaEntradaBean.getNumero());
            query.setParameter(2, bandejaEntradaBean.getCompania());
            query.setParameter(3, bandejaEntradaBean.getContraparte());
            query.setParameter(4, bandejaEntradaBean.getSumilla());
            query.setParameter(5, bandejaEntradaBean.getProceso());
            query.setParameter(6, bandejaEntradaBean.getEstado());
            query.setParameter(7, bandejaEntradaBean.getUsuario());
            query.setParameter(8, fecha);
            query.setParameter(9, bandejaEntradaBean.getPais());

            // Realizamos la llamada al procedimiento
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {

                Traza traza = trazaRepository.findById((Integer) result[0]);
                Alerta alerta = new Alerta();
                TipoAlerta tipoAlerta = new TipoAlerta();
                tipoAlerta.setImagen(hcDocumentoLegalDao.obtenerAlerta((Integer) result[13]));
                alerta.setTipoAlerta(tipoAlerta);
                alerta.setGrid(null);

                listadoBandejaEntrada.add(new BandejaBean((Integer) result[0],
                        result[1] != null ? (String) result[1] : "",
                        result[2] != null ? (String) result[2] : "",
                        result[3] != null ? (String) result[3] : "",
                        result[4] != null ? (String) result[4] : "",
                        result[5] != null ? estadoService.obtenerEstadoPorCodigoCharacter((Character) result[5]) : "",
                        result[6] != null ? (String) result[6] : "",
                        result[7] != null ? (String) result[7] : "",
                        result[8] != null ? (String) result[8] : "",
                        result[9] != null ? (Character) result[9] : null,
                        result[10] != null ? (Boolean) result[10] : null,
                        result[11] != null ? (String) result[11] : "",
                        result[12] != null ? (String) result[12] : "",
                        alerta));
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return listadoBandejaEntrada;
    }

    @Override
    public List<MisPendientesBean> listadoBandejaPendientes(BandejaPendientesFilter bandejaPendientesFilter) {

        List<MisPendientesBean> listadoBandejaPendientes = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("stpr_Listado_BandejaMisPendientes");

            // Register inputs
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);

            // Set parameters
            query.setParameter(1, bandejaPendientesFilter.getEstado());
            query.setParameter(2, bandejaPendientesFilter.getUsuario());
            query.setParameter(3, bandejaPendientesFilter.getNumero());
            query.setParameter(4, bandejaPendientesFilter.getCompania());
            query.setParameter(5, bandejaPendientesFilter.getContraparte());
            query.setParameter(6, bandejaPendientesFilter.getSumilla());
            query.setParameter(7, bandejaPendientesFilter.getProceso());

            // Realizamos la llamada al procedimiento
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                listadoBandejaPendientes.add(new MisPendientesBean((Integer) result[0],
                        result[1] != null ? (String) result[1] : "",
                        result[2] != null ? (String) result[2] : "",
                        result[3] != null ? (String) result[3] : "",
                        result[4] != null ? (String) result[4] : "",
                        result[5] != null ? estadoService.obtenerEstadoPorCodigoCharacter((Character) result[5]) : "",
                        result[6] != null ? DateUtil.convertDateToString((Date) result[6], DateUtil.FORMAT_DATE) : "",
                        result[7] != null ? DateUtil.convertDateToString((Date) result[7], DateUtil.FORMAT_DATE) : "",
                        result[8] != null ? (String) result[8] : ""));
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return listadoBandejaPendientes;
    }

    @Override
    public List<MisPendientesBean> listadoBandejaMisSolicitudes(BandejaMisSolicitudesFilter bandejaMisSolicitudesFilter) {

        List<MisPendientesBean> listadoBandejaMisSolicitudes = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("stpr_Listado_BandejaMisSolicitudes");

            // Register inputs
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);

            // Set parameters
            query.setParameter(1, bandejaMisSolicitudesFilter.getEstado());
            query.setParameter(2, bandejaMisSolicitudesFilter.getUsuario());
            query.setParameter(3, bandejaMisSolicitudesFilter.getNumero());
            query.setParameter(4, bandejaMisSolicitudesFilter.getCompania());
            query.setParameter(5, bandejaMisSolicitudesFilter.getContraparte());
            query.setParameter(6, bandejaMisSolicitudesFilter.getSumilla());
            query.setParameter(7, bandejaMisSolicitudesFilter.getProceso());

            // Realizamos la llamada al procedimiento
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {

                listadoBandejaMisSolicitudes.add(new MisPendientesBean((Integer) result[0],
                        result[1] != null ? (String) result[1] : "",
                        result[2] != null ? (String) result[2] : "",
                        result[3] != null ? (String) result[3] : "",
                        result[4] != null ? (String) result[4] : "",
                        result[5] != null ? estadoService.obtenerEstadoPorCodigoCharacter((Character) result[5]) : "",
                        result[6] != null ? (String) result[6] : "",
                        result[7] != null ? (String) result[7] : "",
                        result[8] != null ? (String) result[8] : ""));
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return listadoBandejaMisSolicitudes;
    }

    @Override
    public List<SolicitudesPorEnviarBean> listadoBandejaSolicitudesPorEnviar(BandejaSolicitudesPorEnviarFilter bandejaSolicitudesPorEnviarFilter) {

        List<SolicitudesPorEnviarBean> listadoBandejaSolicitudesPorEnviar = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("stpr_Listado_BandejaSolicitudesPorEnviar");

            // Register inputs
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);

            // Set parameters
            query.setParameter(1, bandejaSolicitudesPorEnviarFilter.getUsuario());
            query.setParameter(2, bandejaSolicitudesPorEnviarFilter.getNumero());
            query.setParameter(3, bandejaSolicitudesPorEnviarFilter.getCompania());
            query.setParameter(4, bandejaSolicitudesPorEnviarFilter.getContraparte());
            query.setParameter(5, bandejaSolicitudesPorEnviarFilter.getProceso());

            // Realizamos la llamada al procedimiento
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                listadoBandejaSolicitudesPorEnviar.add(new SolicitudesPorEnviarBean((Integer) result[0],
                        result[1] != null ? (String) result[1] : "",
                        result[2] != null ? (String) result[2] : "",
                        result[3] != null ? (String) result[3] : "",
                        result[4] != null ? DateUtil.convertDateToString((Date) result[4], DateUtil.FORMAT_DATE) : "",
                        (Integer) result[5],
                        result[6] != null ? (String) result[6] : ""));
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return listadoBandejaSolicitudesPorEnviar;
    }
}