package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcVisado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HcVisadoRepository extends JpaRepository<HcVisado, Integer> {
    @Query("SELECT v FROM HcVisado v WHERE v.documentoLegal.id=:idDocumentoLegal ORDER BY v.secuencia DESC")
    HcVisado obtenerUltimoVisado(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query("SELECT v FROM HcVisado v WHERE v.documentoLegal.id=:idDocumentoLegal ORDER BY v.secuencia DESC")
    List<HcVisado> obtenerUltimoVisadoList(@Param("idDocumentoLegal") Integer idDocumentoLegal);
}