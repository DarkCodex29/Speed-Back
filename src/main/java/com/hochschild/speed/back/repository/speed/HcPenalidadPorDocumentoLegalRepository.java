package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcPenalidadPorDocumentoLegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcPenalidadPorDocumentoLegalRepository extends JpaRepository<HcPenalidadPorDocumentoLegal, Integer> {
    @Query("SELECT e FROM HcPenalidadPorDocumentoLegal e WHERE e.id = :idHcPenalidadPorDocumentoLegal")
    HcPenalidadPorDocumentoLegal findById(@Param("idHcPenalidadPorDocumentoLegal") Integer idHcPenalidadPorDocumentoLegal);
    @Query("select e from HcPenalidadPorDocumentoLegal e where e.documentoLegal.expediente.id = :idExpediente")
    List<HcPenalidadPorDocumentoLegal> obtenerPorIdExpediente(@Param("idExpediente") Integer idExpediente);
}