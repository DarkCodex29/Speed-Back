package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TipoDocumentoPorProceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoDocumentoPorProcesoRepository extends JpaRepository<TipoDocumentoPorProceso, Integer> {
    @Query("SELECT td FROM TipoDocumentoPorProceso td WHERE td.id.idProceso = :idProceso")
    List<TipoDocumentoPorProceso> getTipoDocumentoPorProceso(@Param("idProceso") Integer idProceso);

    @Modifying
    @Query("DELETE FROM TipoDocumentoPorProceso ug WHERE ug.id.idProceso = :idProceso")
    void eliminarTipoDocumentosPorProceso(@Param("idProceso") Integer idProceso);
}