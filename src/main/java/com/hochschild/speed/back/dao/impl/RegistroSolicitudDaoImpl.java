package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.RegistroSolicitudDao;
import com.hochschild.speed.back.model.bean.registrarSolicitud.ContratoAdendaBean;
import com.hochschild.speed.back.service.impl.RegistroSolicitudServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class RegistroSolicitudDaoImpl implements RegistroSolicitudDao{

    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(RegistroSolicitudDaoImpl.class.getName());

    private final EntityManager entityManager;

    @Autowired
    public RegistroSolicitudDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ContratoAdendaBean> getContratoByAdenda(String numeroSumilla) {

        try {
            StoredProcedureQuery storedProcedureQuery = entityManager
                    .createStoredProcedureQuery("stpr_ObtenerContratoByAdenda");

            // Register inputs
            storedProcedureQuery.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);

            // Set parameters
            storedProcedureQuery.setParameter(1, numeroSumilla);

            // Realizamos la llamada al procedimiento
            storedProcedureQuery.execute();

            @SuppressWarnings("unchecked")
            List<ContratoAdendaBean> result = storedProcedureQuery.getResultList();

            return result;

        }catch (Exception e){
            LOGGER.info(e.getMessage());
            return null;
        }
    }
}