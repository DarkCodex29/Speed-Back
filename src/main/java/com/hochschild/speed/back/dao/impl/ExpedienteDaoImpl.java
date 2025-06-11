package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.ExpedienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Repository
public class ExpedienteDaoImpl implements ExpedienteDao {

    private final EntityManager entityManager;

    @Autowired
    public ExpedienteDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String obtenerNumeracionExpediente() {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("GENERAR_NUMERACION_EXPEDIENTE");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);

        // Set parameters
        //storedProcedureQuery.setParameter(1, "");

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        Object obj = storedProcedureQuery.getOutputParameterValue(1);
        System.out.println(obj.toString());
        if(obj != null){
            return obj.toString();
        }
        return null;
    }
}