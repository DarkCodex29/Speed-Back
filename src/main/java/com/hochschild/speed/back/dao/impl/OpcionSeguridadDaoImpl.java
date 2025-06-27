package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.OpcionSeguridadDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OpcionSeguridadDaoImpl implements OpcionSeguridadDao {

    private final EntityManager entityManager;

    @Autowired
    public OpcionSeguridadDaoImpl(@Qualifier("authEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Long> getOpcionesSeguridad(Integer idAplicacion, String idUsuario) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("stpr_ObtenerOpcionesSeguridad");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        // Set parameters
        storedProcedureQuery.setParameter(1, idAplicacion);
        storedProcedureQuery.setParameter(2, idUsuario);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        @SuppressWarnings("unchecked")
        List<Short> result = storedProcedureQuery.getResultList();

        return convertResult(result);
    }

    @Override
    public List<Long> getOpcionesSinUsuario(Integer idAplicacion, String idRol) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("stpr_ObtenerRolesSinUsuario");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        // Set parameters
        storedProcedureQuery.setParameter(1, idAplicacion);
        storedProcedureQuery.setParameter(2, idRol);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        @SuppressWarnings("unchecked")
        List<Short> result = storedProcedureQuery.getResultList();

        return convertResult(result);
    }

    List<Long> convertResult(List<Short> result) {

        List<Long> convertedResult = new ArrayList<>();

        for (Short s : result)
            convertedResult.add(Long.valueOf(s));

        return convertedResult;
    }

    @Override
    public List<String> getRolesPorUsuario(Integer idAplicacion, String idPuesto) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("stpr_ObtenerRolesPorUsuario");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        // Set parameters
        storedProcedureQuery.setParameter(1, idAplicacion);
        storedProcedureQuery.setParameter(2, idPuesto);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        @SuppressWarnings("unchecked")
        List<String> result = storedProcedureQuery.getResultList();

        return result;
    }

    @Override
    public List<String> getValoresOrganizacionales(Integer idAplicacion, String usuario) {

        List<String> listaVO = new ArrayList<>();

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("stpr_ObtenerTodosValoresOrganizacionalesSeguridad");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        // Set parameters
        storedProcedureQuery.setParameter(1, idAplicacion);
        storedProcedureQuery.setParameter(2, usuario);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        @SuppressWarnings("unchecked")
        List<String[]> result = storedProcedureQuery.getResultList();

        if (result != null && !result.isEmpty()) {
            for (Object[] fila : result) {
                if (StringUtils.countMatches(fila[1].toString(), ",") == 3) {
                    listaVO.add(fila[1].toString());
                } else {
                    if (StringUtils.countMatches(fila[0].toString(), ",") == 3) {
                        listaVO.add(fila[0].toString());
                    }
                }
            }
        }
        return listaVO;
    }
}