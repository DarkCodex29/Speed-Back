package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcNumeracion;
import com.hochschild.speed.back.model.domain.speed.Numeracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HcNumeracionRepository extends JpaRepository<HcNumeracion, Integer> {
    @Query("select e from HcNumeracion e where e.id = :idHcNumeracion")
    HcNumeracion findById(@Param("idHcNumeracion") Integer idHcNumeracion);
    @Query("SELECT hc.anio FROM HcNumeracion hc GROUP BY hc.anio ORDER BY hc.anio")
    List<Integer> getNumeracionAnio();
    @Query("SELECT n FROM HcNumeracion n WHERE n.contrato.id=:idContrato")
    HcNumeracion obtenerNumeracionContrato(@Param("idContrato") Integer idContrato);
}