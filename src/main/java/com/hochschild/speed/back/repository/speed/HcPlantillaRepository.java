package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcPlantilla;
import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcPlantillaRepository extends JpaRepository<HcPlantilla, Integer> {
    @Query("SELECT e FROM HcPlantilla e WHERE " +
          "(e.nombre LIKE %:nombre% OR :nombre IS NULL) AND " +
          "(e.tipoContrato.nombre LIKE %:tipoContrato% OR :tipoContrato IS NULL) AND " +
          "e.estado = 'A' ORDER BY e.id DESC")
    List<HcPlantilla> list(@Param("nombre") String nombre,
                       @Param("tipoContrato") String tipoContrato);
    @Query("SELECT e FROM HcPlantilla e WHERE e.id = :idHcPlantilla")
    HcPlantilla findById(@Param("idHcPlantilla") Integer idHcPlantilla);
    @Query("SELECT new HcPlantilla(p.id,p.nombre,p.ruta) FROM HcPlantilla p WHERE p.tipoContrato.id = :idHcTipoContrato AND p.estado = :estado")
    List<HcPlantilla> findLstBy(@Param("idHcTipoContrato") Integer idHcTipoContrato, @Param("estado") Character estado);
    @Query("SELECT e FROM HcTipoContrato e ORDER BY e.nombre")
    List<HcTipoContrato> listTipoContrato();
}