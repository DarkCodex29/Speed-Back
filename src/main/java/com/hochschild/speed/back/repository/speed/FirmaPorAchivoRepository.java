package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.FirmaPorArchivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FirmaPorAchivoRepository extends JpaRepository<FirmaPorArchivo, Integer> {

    @Query("SELECT f FROM FirmaPorArchivo f WHERE f.archivo.id = :idArchivo AND f.version = :version")
    FirmaPorArchivo obtenerPorArchivoVersion(@Param("idArchivo") Integer idArchivo, @Param("version") String version);
}
