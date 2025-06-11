package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudVigenteBean;
import com.hochschild.speed.back.model.bean.dashboard.area.GeneralAreaBean;
import com.hochschild.speed.back.model.bean.dashboard.area.SolicitudBean;
import com.hochschild.speed.back.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.hochschild.speed.back.dao.DashboardAreaDao;
import org.springframework.stereotype.Repository;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DashboardAreaDaoImpl implements DashboardAreaDao {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DashboardAreaDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public DashboardAreaDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<GeneralAreaBean> solicitudesGenerales(String usuario) {

        List<GeneralAreaBean> generalAdcBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Area_General");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                GeneralAreaBean general = new GeneralAreaBean();
                general.setProceso((String) result[0]);
                general.setEstado((String) result[1]);
                general.setCantidad((Integer) result[2]);
                general.setPorcentaje((BigDecimal) result[3]);
                generalAdcBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return generalAdcBean;
    }

    @Override
    public List<AreaSolicitudBean> solicitudesPorAbogado(String usuario) {

        List<AreaSolicitudBean> areaSolicitudBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Area_EstadoSolicitud");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                AreaSolicitudBean general = new AreaSolicitudBean();
                general.setAbogado((String) result[0]);
                general.setEstado((String) result[1]);
                general.setCantidad((Integer) result[2]);
                general.setPorcentaje((BigDecimal) result[3]);
                areaSolicitudBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return areaSolicitudBean;
    }

    @Override
    public List<AreaSolicitudVigenteBean> solicitudesVigente(String usuario) {
        List<AreaSolicitudVigenteBean> areaSolicitudVigenteBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Area_SolicitudesVigente");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                AreaSolicitudVigenteBean general = new AreaSolicitudVigenteBean();
                general.setSolicitud((String) result[0]);
                general.setAbogado((String) result[1]);
                general.setSolicitante((String) result[2]);
                general.setContraparte((String) result[3]);
                general.setSumilla((String) result[4]);
                general.setFechaVencimiento(result[5] != null ? DateUtil.convertDateToString((Date) result[5], DateUtil.FORMAT_DATE) : "");
                general.setDiasPorVencer((Integer) result[6]);
                areaSolicitudVigenteBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return areaSolicitudVigenteBean;
    }

    @Override
    public List<SolicitudBean> solicitudes(String usuario) {
        List<SolicitudBean> solicitudBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Area_Solicitudes");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                SolicitudBean general = new SolicitudBean();
                general.setSolicitud((String) result[0]);
                general.setAbogado((String) result[1]);
                general.setContraparte((String) result[2]);
                general.setEstado((String) result[3]);
                general.setFechaEnvio(result[4] != null ? DateUtil.convertDateToString((Date) result[4], DateUtil.FORMAT_DATE) : "");
                general.setDiasEnvio((Integer) result[5]);
                solicitudBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return solicitudBean;
    }
}