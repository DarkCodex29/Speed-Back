package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.GrupoPersonalDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GrupoPersonalDaoImpl implements GrupoPersonalDao {

    private static final Logger LOGGER = Logger.getLogger(GrupoPersonalDaoImpl.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> obtenerCorreosGrupoPersonal() {
        List<String> correosGrupoPersonal = new ArrayList<>();
        
        try {
            LOGGER.info("Ejecutando procedimiento almacenado: SPEED_ADM_ListarGrupoPersonal");
            
            StoredProcedureQuery storedProcedureQuery = entityManager
                    .createStoredProcedureQuery("SPEED_ADM_ListarGrupoPersonal");

            // Ejecutar el procedimiento
            storedProcedureQuery.execute();

            // Obtener los resultados - puede ser String o Object[]
            @SuppressWarnings("unchecked")
            List<Object> resultList = storedProcedureQuery.getResultList();
            
            LOGGER.info("Procedimiento ejecutado exitosamente. Registros obtenidos: " + 
                       (resultList != null ? resultList.size() : 0));

            if (resultList != null && !resultList.isEmpty()) {
                for (Object resultado : resultList) {
                    if (resultado != null) {
                        String correo = null;
                        
                        if (resultado instanceof Object[]) {
                            // Si viene como array, tomar el primer elemento
                            Object[] row = (Object[]) resultado;
                            if (row.length > 0 && row[0] != null) {
                                correo = row[0].toString().trim();
                            }
                        } else {
                            // Si viene como String directamente
                            correo = resultado.toString().trim();
                        }
                        
                        if (correo != null && !correo.isEmpty()) {
                            correosGrupoPersonal.add(correo);
                            LOGGER.info("Correo agregado desde DB: " + correo);
                        }
                    }
                }
            } else {
                LOGGER.warn("El procedimiento almacenado no devolvio resultados");
            }

        } catch (Exception e) {
            LOGGER.error("Error al ejecutar procedimiento SPEED_ADM_ListarGrupoPersonal", e);
            // NO usar fallback - lanzar excepción para que se maneje a nivel superior
            throw new RuntimeException("Error al obtener correos del procedimiento almacenado: " + e.getMessage(), e);
        }

        LOGGER.info("Total de correos obtenidos desde DB: " + correosGrupoPersonal.size());
        return correosGrupoPersonal;
    }
} 