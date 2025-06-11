package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoProcesoRepository extends JpaRepository<TipoProceso, Integer> {
    @Query("SELECT e FROM TipoProceso e WHERE e.id = :idTipoProceso")
    TipoProceso findById(@Param("idTipoProceso") Integer idTipoProceso);
    @Query("SELECT p FROM TipoProceso p WHERE p.estado='A'")
    List<TipoProceso> list();
}