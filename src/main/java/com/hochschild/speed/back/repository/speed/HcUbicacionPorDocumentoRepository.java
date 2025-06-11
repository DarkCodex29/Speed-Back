package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.domain.speed.HcUbicacionPorDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcUbicacionPorDocumentoRepository extends JpaRepository<HcUbicacionPorDocumento, Integer> {
    @Query("SELECT e FROM HcUbicacionPorDocumento e WHERE e.id = :idHcUbicacionPorDocumento")
    HcUbicacionPorDocumento findById(@Param("idHcUbicacionPorDocumento") Integer idHcUbicacionPorDocumento);
    @Query("select e from HcUbicacionPorDocumento e where e.id.documentoLegal.id = :idDocumentoLegal")
    List<HcUbicacionPorDocumento> obtenerTodoUbicacionesDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);
    @Query("SELECT upd.id.ubicacion FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id=:idDocumentoLegal")
    List<HcUbicacion> obtenerUbicacionesDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query("SELECT new java.lang.Long(COUNT(upd)) " +
            "FROM HcUbicacionPorDocumento upd " +
            "WHERE upd.id.documentoLegal.id=:idDocumentoLegal " +
            "AND upd.id.ubicacion.codigo=:codigoUbicacion")
    Long countByIdDocumentoLegalAndCodigoUbicacion(@Param("idDocumentoLegal") Long idDocumentoLegal, @Param("codigoUbicacion") String codigoUbicacion);

    @Query("SELECT new java.lang.String(upd.id.ubicacion.nombre) FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id=:idDocumentoLegal")
    List<String> obtenerNombreUbicacion(@Param("idDocumentoLegal") Integer idDocumentoLegal);
}