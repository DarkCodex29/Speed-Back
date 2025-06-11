package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
    @Query("SELECT ad FROM Area ad WHERE ad.id = :idArea")
    Area findById(@Param("idArea") Integer idArea);
    @Query("SELECT a FROM Area a ORDER BY a.nombre")
    List<Area> buscarAreas();
    @Query("SELECT e FROM Area e WHERE " +
            "(e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.id DESC")
    List<Area> list(@Param("nombre") String nombre);
}