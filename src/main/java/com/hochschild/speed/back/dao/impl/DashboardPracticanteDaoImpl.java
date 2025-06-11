package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.DashboardPracticanteDao;
import com.hochschild.speed.back.model.bean.dashboard.abogado.ClienteInternoBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.EstadoSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class DashboardPracticanteDaoImpl implements DashboardPracticanteDao {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DashboardPracticanteDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public DashboardPracticanteDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public GeneralBean solicitudesGenerales(Integer tipo, String usuario) {

        GeneralBean generalBean = new GeneralBean();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Abogado_General");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                generalBean.setProceso((String) result[0]);
                generalBean.setEstado((String) result[1]);
                generalBean.setCantidad((Integer) result[2]);
                generalBean.setPorcentaje((BigDecimal) result[3]);
                generalBean.setColor((String) result[4]);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return generalBean;
    }

    @Override
    public EstadoSolicitudBean solicitudesPorAbogadoResponsable(Integer tipo, String usuario) {

        EstadoSolicitudBean estadoSolicitudBean = new EstadoSolicitudBean();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Abogado_EstadoSolicitud");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                estadoSolicitudBean.setAbogado((String) result[0]);
                estadoSolicitudBean.setEstado((String) result[1]);
                estadoSolicitudBean.setCantidad((Integer) result[2]);
                estadoSolicitudBean.setColor((String) result[3]);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return estadoSolicitudBean;
    }

    @Override
    public ClienteInternoBean clientesInternos(Integer tipo, String usuario) {

        ClienteInternoBean clienteInternoBean = new ClienteInternoBean();

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_Abogado_ClienteInterno");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
            query.setParameter(1, tipo);
            query.setParameter(2, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                clienteInternoBean.setArea((String) result[0]);
                clienteInternoBean.setAbogado((String) result[1]);
                clienteInternoBean.setCantidad((Integer) result[2]);
                clienteInternoBean.setPorcentaje((BigDecimal) result[3]);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return clienteInternoBean;
    }
}