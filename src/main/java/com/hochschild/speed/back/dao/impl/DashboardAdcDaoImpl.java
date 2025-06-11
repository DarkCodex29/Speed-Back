package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.model.bean.dashboard.adc.ProcesoFirmaBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.ProcesoVisadoBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.GeneralAdcBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.hochschild.speed.back.dao.DashboardAdcDao;
import org.springframework.stereotype.Repository;
import javax.persistence.StoredProcedureQuery;
import com.hochschild.speed.back.util.DateUtil;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DashboardAdcDaoImpl implements DashboardAdcDao{
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DashboardAdcDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public DashboardAdcDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<GeneralAdcBean> solicitudesGenerales() {

        List<GeneralAdcBean> generalBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_ADC_GeneralGrupo");
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                GeneralAdcBean general = new GeneralAdcBean();
                general.setGrupo((String) result[0]);
                general.setEstado((String) result[1]);
                general.setCantidad((Integer) result[2]);
                general.setPorcentaje((BigDecimal) result[3]);
                generalBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return generalBean;
    }

    @Override
    public List<GeneralAdcBean> solicitudesPorGrupo() {

        List<GeneralAdcBean> generalAdcBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_ADC_GeneralGrupo");
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                GeneralAdcBean general = new GeneralAdcBean();
                general.setGrupo((String) result[0]);
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
    public List<ProcesoFirmaBean> procesoPorFirma() {

        List<ProcesoFirmaBean> procesoFirmaBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_ADC_SolicitudesFirma");
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                ProcesoFirmaBean procesoFirma = new ProcesoFirmaBean();
                procesoFirma.setSolicitud((String) result[0]);
                procesoFirma.setAbogado((String) result[1]);
                procesoFirma.setContraparte((String) result[2]);
                procesoFirma.setFechaEnvio(result[3] != null ? DateUtil.convertDateToString((Date) result[3], DateUtil.FORMAT_DATE) : "");
                procesoFirma.setDiasEnvio((Integer) result[4]);
                procesoFirmaBean.add(procesoFirma);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return procesoFirmaBean;
    }

    @Override
    public List<ProcesoVisadoBean> procesoPorVisado() {

        List<ProcesoVisadoBean> procesoVisadoBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_ADC_SolicitudesVisado");
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                ProcesoVisadoBean procesoVisado = new ProcesoVisadoBean();
                procesoVisado.setSolicitud((String) result[0]);
                procesoVisado.setAbogado((String) result[1]);
                procesoVisado.setSolicitante((String) result[2]);
                procesoVisado.setContraparte((String) result[3]);
                procesoVisado.setSumilla((String) result[4]);
                procesoVisado.setVisadorActual((String) result[5]);
                procesoVisado.setFechaEnvio(result[6] != null ? DateUtil.convertDateToString((Date) result[6], DateUtil.FORMAT_DATE) : "");
                procesoVisado.setDiasEnvio((Integer) result[7]);
                procesoVisadoBean.add(procesoVisado);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return procesoVisadoBean;
    }
}