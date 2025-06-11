package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.ArchivoNotificarDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class ArchivoNotificarDaoImpl implements ArchivoNotificarDao {

	private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(ArchivoNotificarDaoImpl.class.getName());

	private final EntityManager entityManager;

	@Autowired
	public ArchivoNotificarDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Integer obtenerIdArchivoNotificar(Integer idDocumentoLegal) {
		Integer idArchivo = null;
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("SPEED_Archivo_ObtenerId");

			// Register inputs
			storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

			// Set parameters
			storedProcedureQuery.setParameter(1, idDocumentoLegal);

			// Realizamos la llamada al procedimiento
			storedProcedureQuery.execute();

			List<Integer> results = storedProcedureQuery.getResultList();

			for (Integer result : results) {
				idArchivo = result;
			}

			return idArchivo;

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			return null;
		}
	}
}