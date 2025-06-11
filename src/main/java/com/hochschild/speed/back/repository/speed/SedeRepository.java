package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    @Query("SELECT e FROM Sede e WHERE e.id = :idSede")
    Sede findById(@Param("idSede") Integer idSede);
    @Query("SELECT s FROM Sede s WHERE s.estado = 'A' ORDER BY s.nombre")
    List<Sede> obtenerActivas();
    @Query("SELECT e FROM Sede e WHERE " +
            "(e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.id DESC")
    List<Sede> list(@Param("nombre") String nombre);
}