package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcFirmaElectronicaDocLegal;
import com.hochschild.speed.back.model.domain.speed.HcFirmaElectronicaRepresentante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FirmaElectronicaRepository extends JpaRepository<HcFirmaElectronicaDocLegal, Integer> {
    @Query("select e from HcFirmaElectronicaDocLegal e where e.id = :idHcFirmaElectronicaDocLegal")
    HcFirmaElectronicaDocLegal findById(@Param("idHcFirmaElectronicaDocLegal") Integer idHcFirmaElectronicaDocLegal);
    @Query("SELECT h FROM HcFirmaElectronicaDocLegal h WHERE h.idDocumentoLegal=:idDocumentoLegal")
    HcFirmaElectronicaDocLegal obtenerPorIdDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);
    @Query("SELECT h FROM HcFirmaElectronicaRepresentante h WHERE h.idHcFirmaElectronicaDocLegal=:idFirmaElectronica")
    List<HcFirmaElectronicaRepresentante> obtenerPorIdFirmaElectronica(@Param("idFirmaElectronica") Integer idFirmaElectronica);
    @Modifying
    @Query("DELETE FROM HcFirmaElectronicaDocLegal h WHERE h.id=:id")
    void eliminarHcFirmaElectronicaDocLegalPorId(@Param("id") Integer id);
    @Modifying
    @Query("DELETE FROM HcHistorialFirmaElectronica h WHERE h.idHcFirmaElectronicaDocLegal=:idHcFirmaElectronicaDocLegal")
    void eliminarHistorialPorIdHcFirmaElectronicaDocLegal(@Param("idHcFirmaElectronicaDocLegal") Integer idHcFirmaElectronicaDocLegal);
    @Modifying
    @Query("DELETE FROM HcFirmaElectronicaRepresentante h WHERE h.idHcFirmaElectronicaDocLegal=:idHcFirmaElectronicaDocLegal")
    void eliminarRepresentantePorIdHcFirmaElectronicaDocLegal(@Param("idHcFirmaElectronicaDocLegal") Integer idHcFirmaElectronicaDocLegal);
}