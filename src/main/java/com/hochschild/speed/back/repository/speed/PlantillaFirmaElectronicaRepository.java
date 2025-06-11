package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.PlantillaFirmaElectronica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantillaFirmaElectronicaRepository extends JpaRepository<PlantillaFirmaElectronica, Integer> {
    @Query("select e from PlantillaFirmaElectronica e where e.id = :idPlantillaFirmaElectronica")
    PlantillaFirmaElectronica findById(@Param("idPlantillaFirmaElectronica") Integer idPlantillaFirmaElectronica);
    @Query("SELECT p FROM PlantillaFirmaElectronica p WHERE p.tipoFirma.id=:idTipoFirma AND p.tipoCliente=:tipoCliente")
    PlantillaFirmaElectronica obtenerPorTipoFirmaYTipoCliente(@Param("idTipoFirma") Integer idTipoFirma, @Param("tipoCliente") String tipoCliente);
}