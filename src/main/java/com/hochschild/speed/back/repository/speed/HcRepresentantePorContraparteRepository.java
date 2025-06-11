package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcRepresentantePorContraparte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcRepresentantePorContraparteRepository extends JpaRepository<HcRepresentantePorContraparte, Integer> {
    @Query("SELECT e FROM HcRepresentantePorContraparte e WHERE e.id = :idHcRepresentantePorContraparte")
    HcRepresentantePorContraparte findById(@Param("idHcRepresentantePorContraparte") Integer idHcRepresentantePorContraparte);
    @Query("select e from HcRepresentantePorContraparte e where e.id.contraparte.id = :idContraparte")
    List<HcRepresentantePorContraparte> obtenerRepresentantesContraparte(@Param("idContraparte") Integer idContraparte);
}