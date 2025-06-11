package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcAlarma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcAlarmaRepository extends JpaRepository<HcAlarma, Integer> {
    @Query("select e from HcAlarma e where e.id = :idHcAlarma")
    HcAlarma findById(@Param("idHcAlarma") Integer idHcAlarma);

    @Query("SELECT a FROM HcAlarma a WHERE a.documentoLegal.id=:id and a.estado = 'A'")
    List<HcAlarma> obtenerAlarmas(@Param("id") Integer id);

    @Query("SELECT a FROM HcAlarma a WHERE a.estado = 'A' ORDER BY a.documentoLegal.id")
    List<HcAlarma> obtenerAlarmasActivas();
}