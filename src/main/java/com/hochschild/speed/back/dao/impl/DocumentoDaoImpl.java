package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.DocumentoDao;
import com.hochschild.speed.back.model.domain.speed.utils.EstadoDocumentoDTO;
import com.hochschild.speed.back.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DocumentoDaoImpl implements DocumentoDao {
    private final EntityManager entityManager;

    @Autowired
    public DocumentoDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    Logger LOG = LoggerFactory.getLogger(DocumentoDaoImpl.class);
    @Override
    public List<EstadoDocumentoDTO> listarEstadosDocumento(String codigoDocumento) {

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("stpr_AsistenteVirtual_EstadoSolicitud");
        // set parameters
        query.registerStoredProcedureParameter("numero", String.class, ParameterMode.IN);
        query.setParameter("numero", codigoDocumento);

        List<Object[]> resultado = new ArrayList<>();
        List<EstadoDocumentoDTO> resultMapped = new ArrayList<>();
        try {
            // execute SP
            query.execute();
            resultado = query.getResultList();
            resultMapped = resultado.stream()
                    .map(result -> {
                        EstadoDocumentoDTO estadoDocumentoDTO = new EstadoDocumentoDTO();
                        estadoDocumentoDTO.setId(Integer.valueOf((String) result[0].toString()));
                        estadoDocumentoDTO.setCodigoEstado(String.valueOf((String) result[1]));
                        estadoDocumentoDTO.setEstado((String) result[2]);
                        estadoDocumentoDTO.setFecha((Date) result[3]);
                        estadoDocumentoDTO.setSituacion(Integer.valueOf((String) result[4].toString()));
                        estadoDocumentoDTO.setFlujo((String) result[5]);
                        return estadoDocumentoDTO;
                    }).collect(Collectors.toList());

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return resultMapped;
    }
}
