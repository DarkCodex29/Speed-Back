package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Accion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccionRepository extends JpaRepository<Accion, Integer> {
    @Query("SELECT ad FROM Accion ad WHERE ad.id = :idAccion")
    Accion findById(@Param("idAccion") Integer idAccion);
}