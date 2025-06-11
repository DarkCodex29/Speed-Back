package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
    @Query("SELECT e FROM TipoDocumento e WHERE e.id = :idTipoDocumento")
    TipoDocumento findById(@Param("idTipoDocumento") Integer idTipoDocumento);
    @Query("SELECT td FROM TipoDocumento td WHERE td.estado= 'A' ORDER BY td.nombre")
    List<TipoDocumento> getTiposActivos();
    @Query("SELECT td  FROM TipoDocumento td WHERE td.id NOT IN (SELECT id.idTipoDocumento FROM TipoDocumentoPorProceso where id.idProceso = :idProceso)")
    List<TipoDocumento> obtenerTipoDocumentoNoAsignados(@Param("idProceso") Integer idProceso);
    @Query("SELECT td FROM TipoDocumento td WHERE td.nombre = :nombre")
    TipoDocumento obtenerTipoDocumentoPorNombre(@Param("nombre") String nombre);
    @Query("SELECT e FROM TipoDocumento e WHERE " +
            " (e.descripcion LIKE %:descripcion% OR :descripcion IS NULL) AND " +
            " (e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.fechaCreacion DESC")
    List<TipoDocumento> list(@Param("descripcion") String descripcion,
                             @Param("nombre") String nombre);
}