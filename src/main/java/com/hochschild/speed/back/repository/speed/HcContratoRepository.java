package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HcContratoRepository extends JpaRepository<HcContrato, Integer> {
    @Query("select e from HcContrato e where e.id = :idHcContrato")
    HcContrato findById(@Param("idHcContrato") Integer idHcContrato);
    @Query("SELECT e FROM HcContrato e WHERE e.id = :idContrato")
    HcContrato findHcContratoById(@Param("idContrato") Integer idContrato);
}