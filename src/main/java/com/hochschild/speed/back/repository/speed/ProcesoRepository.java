package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Proceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcesoRepository extends JpaRepository<Proceso, Integer> {
    @Query("select e from Proceso e where e.estado = 'A' AND e.id = :idProceso")
    Proceso findById(@Param("idProceso") Integer idProceso);
    @Query("select e from Proceso e where e.estado = 'A' AND e.tipoProceso.id = :tipoProceso")
    List<Proceso> findByIdTipoProceso(@Param("tipoProceso") Integer tipoProceso);
    @Query("SELECT p FROM Proceso p WHERE p.estado = 'A' ORDER BY p.nombre")
    List<Proceso> obtenerProcesosActivos();
    @Query("SELECT r FROM Proceso r,Reemplazo u where r not in ELEMENTS(u.procesos) AND u.id =:idProceso ORDER BY r.nombre")
    List<Proceso> obtenerNoAsignadosUsuario(@Param("idProceso") Integer idProceso);
    @Query("SELECT p FROM Proceso p WHERE p.tipoProceso.codigo=:tipo")
    List<Proceso> obtenerPorTipo(@Param("tipo") String tipo);
    @Query("SELECT e FROM Proceso e WHERE " +
            "(e.nombre LIKE %:nombre% OR :nombre IS NULL) AND " +
            "(e.tipoProceso.id = :idTipoProceso OR :idTipoProceso IS NULL) " +
            "ORDER BY e.id DESC")
    List<Proceso> list(@Param("nombre") String nombre,
                       @Param("idTipoProceso") Integer idTipoProceso);
    @Query("SELECT e FROM Proceso e WHERE " +
            "(e.tipoProceso.id = :idTipoProceso OR :idTipoProceso IS NULL) AND " +
            "e.idProcesoPadre IS NULL "+
            "ORDER BY e.id DESC")
    List<Proceso> listOnlyParents(@Param("idTipoProceso") Integer idTipoProceso);

}