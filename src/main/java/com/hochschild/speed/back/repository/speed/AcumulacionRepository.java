package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.AcumulacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AcumulacionRepository extends JpaRepository<AcumulacionDocumento, Integer> {

    @Query("SELECT ad FROM AcumulacionDocumento ad WHERE ad.id = :idAcumulacionDocumento")
    AcumulacionDocumento findById(@Param("idAcumulacionDocumento") Integer idAcumulacionDocumento);

    @Query("SELECT ad FROM AcumulacionDocumento ad WHERE ad.documento.id = :idDocumento ORDER BY ad.fecha")
    AcumulacionDocumento obtenerPrimeraPorDocumento(@Param("idDocumento") Integer idDocumento);
}