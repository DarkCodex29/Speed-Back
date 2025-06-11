package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.DashboardDao;
import com.hochschild.speed.back.model.bean.DashboardBean;
import com.hochschild.speed.back.model.bean.dashboard.ModeloDocumentoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DashboardDaoImpl implements DashboardDao {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DashboardDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public DashboardDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public DashboardBean validarAcceso(String usuario) {
        DashboardBean listadoBean = new DashboardBean();
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_ValidarAcceso");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                listadoBean.setId((Integer) result[0]);
                listadoBean.setTipo((Integer) result[1]);
                listadoBean.setPeriodo((Integer) result[2]);
                listadoBean.setValor1((Integer) result[3]);
                listadoBean.setValor2((Integer) result[4]);
                listadoBean.setValor3((Integer) result[5]);
                listadoBean.setValor4((Integer) result[6]);
                listadoBean.setValor5((Integer) result[7]);
            }

        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return listadoBean;
    }

    @Override
    public List<ModeloDocumentoBean> obtenerModeloDocumentos() {
        List<ModeloDocumentoBean> listado = new ArrayList<>();
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Dashboard_ModeloDocumentos");
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                ModeloDocumentoBean modelo = new ModeloDocumentoBean();
                modelo.setId((Integer) result[0]);
                modelo.setNombre((String) result[1]);
                modelo.setRuta((String) result[2]);
                modelo.setCarpeta((String) result[3]);
                listado.add(modelo);
            }

        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return listado;
    }
}