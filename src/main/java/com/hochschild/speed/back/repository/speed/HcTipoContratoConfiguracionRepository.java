package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcTipoContratoConfiguracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcTipoContratoConfiguracionRepository extends JpaRepository<HcTipoContratoConfiguracion, Integer> {
    @Query("SELECT e FROM HcTipoContratoConfiguracion e WHERE e.id = :idHcTipoContratoConfiguracion")
    HcTipoContratoConfiguracion findById(@Param("idHcTipoContratoConfiguracion") Integer idHcTipoContratoConfiguracion);
    @Query("select e from HcTipoContratoConfiguracion e where e.estado = 'S' AND e.hcTipoContrato.id = :idTipoContrato")
    HcTipoContratoConfiguracion findByIdTipoContrato(@Param("idTipoContrato") Integer idTipoContrato);

    @Query("SELECT h FROM HcTipoContratoConfiguracion h WHERE h.hcTipoContrato.codigo=:codigo and h.estado=:estado")
    List<HcTipoContratoConfiguracion> getListaTipoAdenda(@Param("codigo") Character codigo, @Param("estado") String estado);
}