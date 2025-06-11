package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcPenalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HcPenalidadRepository extends JpaRepository<HcPenalidad, Integer> {

    @Query("SELECT e FROM HcPenalidad e WHERE e.id = :idHcPenalidad")
    HcPenalidad findById(@Param("idHcPenalidad") Integer idHcPenalidad);

    @Query("SELECT e FROM HcPenalidad e WHERE " +
            "(e.descripcion LIKE %:descripcion% OR :descripcion IS NULL) AND " +
            "(e.reiterancia = :reiterancia OR :reiterancia IS NULL) " +
            "ORDER BY e.id DESC")
    List<HcPenalidad> list(@Param("descripcion") String descripcion,
                           @Param("reiterancia") Integer reiterancia);
}