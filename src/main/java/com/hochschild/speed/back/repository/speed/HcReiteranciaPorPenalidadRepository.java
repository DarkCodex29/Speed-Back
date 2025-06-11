package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcReiteranciaPorPenalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcReiteranciaPorPenalidadRepository extends JpaRepository<HcReiteranciaPorPenalidad, Integer> {
    @Query("SELECT e FROM HcReiteranciaPorPenalidad e WHERE e.id = :idHcReiteranciaPorPenalidad")
    HcReiteranciaPorPenalidad findById(@Param("idHcReiteranciaPorPenalidad") Integer idHcReiteranciaPorPenalidad);
    @Query("SELECT e FROM HcReiteranciaPorPenalidad e WHERE e.hcPenalidadPorDocumentoLegal.id = :idPenalidadXDL ORDER BY e.index ASC")
    List<HcReiteranciaPorPenalidad> obtenerPorHcPenalidadPorDocumentoLegal(@Param("idPenalidadXDL") Integer idPenalidadXDL);


}