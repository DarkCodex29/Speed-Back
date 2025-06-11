package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Grid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GridRepository extends JpaRepository<Grid, Integer> {
    @Query("SELECT ad FROM Grid ad WHERE ad.id = :idGrid")
    Grid findById(@Param("idGrid") Integer idGrid);

    @Query("SELECT e FROM Grid e WHERE e.estado = 'A' " +
            "ORDER BY e.nombre")
    List<Grid> list();
}