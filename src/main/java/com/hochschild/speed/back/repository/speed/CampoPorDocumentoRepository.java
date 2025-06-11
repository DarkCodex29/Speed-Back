package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.CampoPorDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampoPorDocumentoRepository extends JpaRepository<CampoPorDocumento, Integer> {
    @Query("SELECT ad FROM CampoPorDocumento ad WHERE ad.id = :idCampoPorDocumento")
    CampoPorDocumento findById(@Param("idCampoPorDocumento") Integer idCampoPorDocumento);

    @Query("select e from CampoPorDocumento e where e.id.campo.id = :idCampo AND e.id.documento.id = :idDocumento")
    CampoPorDocumento obtenerCampoPorDocumento(@Param("idCampo") Integer idCampo,
                                               @Param("idDocumento") Integer idDocumento);
    @Query("select cd From CampoPorDocumento cd where cd.id.documento.id=:idDocumento")
    List<CampoPorDocumento> getCamposPorDocumento(@Param("idDocumento") Integer idDocumento);
}