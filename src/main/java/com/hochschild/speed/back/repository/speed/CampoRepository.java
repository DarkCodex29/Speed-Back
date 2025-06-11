package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Campo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampoRepository extends JpaRepository<Campo, Integer> {
    @Query("SELECT ad FROM Campo ad WHERE ad.id = :idCampo")
    Campo findById(@Param("idCampo") Integer idCampo);

    @Query("select e from Campo e where e.tipoCampo.id = :idTipoDocumento")
    List<Campo> getCamposPorTipoDocumento(@Param("idTipoDocumento") Integer idTipoDocumento);

    @Query("SELECT p FROM Campo p,TipoDocumento u WHERE p NOT IN ELEMENTS(u.campos) AND u.id = :idTipoDocumento AND u.estado = 'A' ORDER BY p.nombre")
    List<Campo> buscarCamposNoAsignados(@Param("idTipoDocumento") Integer idTipoDocumento);

    @Query("SELECT e FROM Campo e WHERE " +
            " (e.descripcion LIKE %:descripcion% OR :descripcion IS NULL) AND " +
            " (e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.fechaCreacion DESC")
    List<Campo> list(@Param("descripcion") String descripcion,
                     @Param("nombre") String nombre);
}