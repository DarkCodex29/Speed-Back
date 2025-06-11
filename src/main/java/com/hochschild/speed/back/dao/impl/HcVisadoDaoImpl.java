package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.HcVisadoDao;
import com.hochschild.speed.back.model.domain.speed.HcVisado;
import com.hochschild.speed.back.model.domain.speed.utils.ElementoListaVisadoDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HcVisadoDaoImpl implements HcVisadoDao {
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(HcVisadoDaoImpl.class);
    private final EntityManager entityManager;

    @Autowired
    public HcVisadoDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public HcVisado obtenerUltimoVisado(Integer idDocumentoLegal) {
        String sql = "SELECT vi FROM HcVisado vi WHERE vi.documentoLegal.id=:idDocumentoLegal ORDER BY vi.secuencia DESC";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idDocumentoLegal", idDocumentoLegal);
        q.setMaxResults(1);
        try{
            return (HcVisado) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public List<ElementoListaVisadoDTO> obtenerListaVisado(String usuario) {
        List<ElementoListaVisadoDTO> listado = new ArrayList<>();
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("OBTENER_LISTA_VISADO");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);

            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                ElementoListaVisadoDTO modelo = new ElementoListaVisadoDTO();
                modelo.setIdDocumentoLegal((Integer) result[0]);
                modelo.setNumeroDocumentoLegal((String) result[1]);
                modelo.setCompania((String) result[2]);
                modelo.setTipoCliente((String) result[3]);
                modelo.setClienteNombre((String) result[4]);
                modelo.setClienteRazonSocial((String) result[5]);
                modelo.setSumilla((String) result[6]);
                modelo.setTipoContrato((String) result[7]);

                listado.add(modelo);
            }

        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return listado;
    }
}