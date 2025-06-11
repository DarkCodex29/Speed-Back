package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {
    @Query("select e from Alerta e where e.id = :idAlerta")
    Alerta findById(@Param("idAlerta") Integer idAlerta);
    @Query("SELECT a FROM Alerta a WHERE a.grid.id=:idGrid ORDER BY a.tipoAlerta.porcentaje")
    List<Alerta> obtenerPorGrid(@Param("idGrid") Integer idGrid);
    @Query("SELECT e FROM Alerta e WHERE " +
            "(e.grid.titulo LIKE %:titulo% OR :titulo IS NULL) AND " +
            "(e.tipoAlerta.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.id DESC")
    List<Alerta> list(@Param("titulo") String titulo,
                      @Param("nombre") String nombre);
}