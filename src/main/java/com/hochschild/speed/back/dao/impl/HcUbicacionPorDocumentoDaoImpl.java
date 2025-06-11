package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.HcUbicacionPorDocumentoDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class HcUbicacionPorDocumentoDaoImpl implements HcUbicacionPorDocumentoDao {
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(BotonesDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public HcUbicacionPorDocumentoDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean documentoContieneUbicacion(Integer idDocumentoLegal, String codigoUbicacion) {
        try {
            String sql = "SELECT COUNT(upd) FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id=:idDocumentoLegal AND upd.id.ubicacion.codigo=:codigoUbicacion ";
            Query q = entityManager.createQuery(sql);
            q.setParameter("idDocumentoLegal", idDocumentoLegal);
            q.setParameter("codigoUbicacion", codigoUbicacion);

            Long count = (Long) q.getSingleResult();

            return count >= 1L;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String obtenerUbicacionesReporte(Integer idDocumentoLegal) {
        String salida = "";
        try{
            String sql = "SELECT new java.lang.String(upd.id.ubicacion.nombre) FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id=:idDocumentoLegal ";
            Query q=entityManager.createQuery(sql);
            q.setParameter("idDocumentoLegal", idDocumentoLegal);

            List<String> ubicaciones=(List<String>) q.getResultList();

            if(ubicaciones != null && !ubicaciones.isEmpty()){
                boolean primero = true;
                for(String ubicacion : ubicaciones){
                    if(primero){
                        primero = false;
                        salida +=ubicacion;
                    }else{
                        salida+=", "+ubicacion;
                    }
                }
            }

        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return salida;
    }
}
