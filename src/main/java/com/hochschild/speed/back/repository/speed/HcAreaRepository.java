package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcAreaRepository extends JpaRepository<HcArea, Integer> {
    @Query("select e from HcArea e where e.id = :idArea AND e.estado = 'A'")
    HcArea findById(@Param("idArea") Integer idArea);
    @Query("select e from HcArea e where e.estado = 'A' ORDER BY e.nombre ASC")
    List<HcArea> listarAreasActivas();
    @Query("select e from HcArea e where e.compania.id = :idCompania AND e.estado = :estado ORDER BY e.nombre ASC")
    List<HcArea> listarAreasPorCompania(@Param("idCompania") Integer idCompania,
                                        @Param("estado") Character estado);
}