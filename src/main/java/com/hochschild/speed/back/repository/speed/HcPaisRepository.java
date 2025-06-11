package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcPais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcPaisRepository extends JpaRepository<HcPais, Integer> {
    @Query("SELECT e FROM HcPais e WHERE e.id = :idHcPais")
    HcPais findById(@Param("idHcPais") Integer idHcPais);
    @Query("SELECT p FROM HcPais p Where p.estado = 'A' ORDER BY p.orden ASC")
    List<HcPais> listarPaises();
}