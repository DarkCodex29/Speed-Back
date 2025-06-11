package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.SistemaOpciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SistemaOpcionesRepository extends JpaRepository<SistemaOpciones, Long> {
    @Query("SELECT s FROM SistemaOpciones s WHERE s.idOpcion IN (:idOpciones) AND s.vigente = :vigente AND s.idPadre IS NULL  ORDER BY CAST(s.orden AS int) ASC")
    List<SistemaOpciones> findByPermissionsHeaders(@Param("idOpciones") List<Long> idOpciones, @Param("vigente") String vigente);
    
    @Query("SELECT s FROM SistemaOpciones s WHERE s.idOpcion IN (:idOpciones) AND s.vigente = :vigente ORDER BY CAST(s.orden AS int) ASC")
    List<SistemaOpciones> findByPermissionsChilds(@Param("idOpciones") List<Long> idOpciones, @Param("vigente") String vigente);

    @Query("SELECT s.idPadre FROM SistemaOpciones s WHERE s.idOpcion IN (:idOpciones) AND s.vigente = :vigente AND s.idPadre IS NOT NULL GROUP BY s.idPadre, s.orden ORDER BY CAST(s.orden AS int) ASC")
    List<Long> findHeadersByPermissions(@Param("idOpciones") List<Long> idOpciones, @Param("vigente") String vigente);
}