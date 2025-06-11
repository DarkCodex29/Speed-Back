package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Integer> {
    @Query("SELECT e FROM TipoNotificacion e WHERE e.id = :idTipoNotificacion")
    TipoNotificacion findById(@Param("idTipoNotificacion") Integer idTipoNotificacion);
    @Query("select e from TipoNotificacion e where e.estado = 'A' AND e.codigo = :codigo")
    TipoNotificacion obtenerPorCodigo(@Param("codigo") String codigo);
    @Query("SELECT e FROM TipoNotificacion e WHERE " +
            " (e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.id DESC")
    List<TipoNotificacion> list(@Param("nombre") String nombre);
}