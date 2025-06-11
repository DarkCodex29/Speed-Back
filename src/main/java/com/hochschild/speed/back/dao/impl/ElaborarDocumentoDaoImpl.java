package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.DashboardAreaDao;
import com.hochschild.speed.back.dao.ElaborarDocumentoDao;
import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudVigenteBean;
import com.hochschild.speed.back.model.bean.dashboard.area.GeneralAreaBean;
import com.hochschild.speed.back.model.bean.dashboard.area.SolicitudBean;
import com.hochschild.speed.back.model.bean.elaborarDocumento.UsuarioDestinatarioBean;
import com.hochschild.speed.back.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ElaborarDocumentoDaoImpl implements ElaborarDocumentoDao {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ElaborarDocumentoDaoImpl.class.getName());
    private final EntityManager entityManager;

    @Autowired
    public ElaborarDocumentoDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public String ubicacionUsuarioBean(String usuario) {

        String ubicacionUsuario = null;

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SPEED_Contractual_UbicacionUsuario");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, usuario);
            query.execute();

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                ubicacionUsuario = (String) result[1];
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return ubicacionUsuario;
    }
}