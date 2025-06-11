package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoAlertaRepository extends JpaRepository<TipoAlerta, Integer> {
    @Query("SELECT e FROM TipoAlerta e WHERE e.id = :idTipoAlerta")
    TipoAlerta findById(@Param("idTipoAlerta") Integer idTipoAlerta);

    @Query("SELECT e FROM TipoAlerta e WHERE " +
            "(e.nombre LIKE %:nombre% OR :nombre IS NULL) AND " +
            "(e.porcentaje = :porcentaje OR :porcentaje IS NULL) " +
            "ORDER BY e.fechaCreacion DESC")
    List<TipoAlerta> list(@Param("nombre") String nombre,
                          @Param("porcentaje") Integer porcentaje);
}