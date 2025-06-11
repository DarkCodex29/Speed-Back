package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.UsuarioDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {
    private static final Logger log = LoggerFactory.getLogger(UsuarioDaoImpl.class);
    private final EntityManager entityManager;

    @Autowired
    public UsuarioDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String obtenerCorreoUsuarioSCA(String usuario) {
        try{
            Query q=entityManager.createNativeQuery("{call OBTENER_CORREO_USUARIO(?)}");

            q.setParameter(1, usuario);

            Object respuesta = q.getSingleResult();

            if(respuesta != null && !StringUtils.isBlank(respuesta.toString())){
                return respuesta.toString();
            }
        }catch(Exception e){
            log.error("No se pudo obtener el correo del usuario "+usuario,e);
        }

        return "";    }
}
