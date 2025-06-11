package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcAdenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcAdendaRepository extends JpaRepository<HcAdenda, Integer> {
    @Query("select e from HcAdenda e where e.id = :idHcAdenda")
    HcAdenda findById(@Param("idHcAdenda") Integer idHcAdenda);
    @Query("SELECT MAX(ad.secuencia) FROM HcAdenda ad WHERE ad.contrato.id = :idContrato")
    Integer obtenerSecuenciaAdendaPorContrato(@Param("idContrato") Integer idContrato);
    @Query("SELECT ad FROM HcAdenda ad WHERE ad.contrato.id = :idContrato AND ad.secuencia = :secuencia AND ad.secuencia <> 0")
    HcAdenda obtenerByIdContratoAndSecuencia(@Param("idContrato") Integer idContrato, @Param("secuencia") Integer secuencia);
    @Query("SELECT ad FROM HcAdenda ad WHERE ad.contrato.id = :idContrato")
    List<HcAdenda> obtenerAdendasByIdContrato(@Param("idContrato") Integer idContrato);
}