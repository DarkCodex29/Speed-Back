package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.ResponsableRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsableRolRepository extends JpaRepository<ResponsableRol, Integer> {
    @Query("SELECT ug FROM ResponsableRol ug WHERE ug.id.idProceso = :idProceso")
    ResponsableRol findById(@Param("idProceso") Integer idProceso);
    @Modifying
    @Query("DELETE FROM ResponsableRol ug WHERE ug.id.idProceso = :idProceso")
    void eliminarResponsableRolesPorProceso(@Param("idProceso") Integer idProceso);
}