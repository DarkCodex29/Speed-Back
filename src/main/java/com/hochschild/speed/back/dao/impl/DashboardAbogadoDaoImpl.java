package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.model.bean.dashboard.abogado.ClienteInternoBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.EstadoSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralElaboradoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.hochschild.speed.back.dao.DashboardAbogadoDao;
import org.springframework.stereotype.Repository;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DashboardAbogadoDaoImpl implements DashboardAbogadoDao {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DashboardAbogadoDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public DashboardAbogadoDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<GeneralBean> solicitudesGenerales(Integer tipo, String usuario) {

        List<GeneralBean> generalBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Abogado_General");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                GeneralBean general = new GeneralBean();
                general.setProceso((String) result[0]);
                general.setEstado((String) result[1]);
                general.setCantidad((Integer) result[2]);
                general.setPorcentaje((BigDecimal) result[3]);
                general.setColor((String) result[4]);
                generalBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return generalBean;
    }

    @Override
    public List<EstadoSolicitudBean> solicitudesPorAbogadoResponsable(Integer tipo, String usuario) {

        List<EstadoSolicitudBean> estadoSolicitudBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Abogado_EstadoSolicitud");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                EstadoSolicitudBean estadoSolicitud = new EstadoSolicitudBean();
                estadoSolicitud.setAbogado((String) result[0]);
                estadoSolicitud.setEstado((String) result[1]);
                estadoSolicitud.setCantidad((Integer) result[2]);
                estadoSolicitud.setColor((String) result[3]);
                estadoSolicitudBean.add(estadoSolicitud);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return estadoSolicitudBean;
    }

    @Override
    public List<ClienteInternoBean> clientesInternos(Integer tipo, String usuario) {

        List<ClienteInternoBean> clienteInternoBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Abogado_ClienteInterno");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                ClienteInternoBean clienteInterno = new ClienteInternoBean();
                clienteInterno.setArea((String) result[0]);
                clienteInterno.setAbogado((String) result[1]);
                clienteInterno.setCantidad((Integer) result[2]);
                clienteInterno.setPorcentaje((BigDecimal) result[3]);
                clienteInternoBean.add(clienteInterno);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return clienteInternoBean;
    }

    @Override
    public List<GeneralElaboradoBean> solicitudesGeneralesElaborado(Integer tipo, String usuario) {

        List<GeneralElaboradoBean> generalElaboradoBean = new ArrayList<>();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Elaborado_General_Area_Ubicacion");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                GeneralElaboradoBean general = new GeneralElaboradoBean();
                general.setEstado((String) result[0]);
                general.setCantidad((Integer) result[1]);
                general.setPorcentaje((BigDecimal) result[2]);
                general.setColor((String) result[3]);
                general.setUbicacion((String) result[4]);
                general.setIdUbicacion((Integer) result[5]);
                generalElaboradoBean.add(general);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return generalElaboradoBean;
    }
}