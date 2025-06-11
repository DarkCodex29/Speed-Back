package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.ResponsableUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsableUsuarioRepository extends JpaRepository<ResponsableUsuario, Integer> {
    @Query("SELECT ug FROM ResponsableUsuario ug WHERE ug.id.idProceso = :idProceso")
    ResponsableUsuario findById(@Param("idProceso") Integer idProceso);
    @Modifying
    @Query("DELETE FROM ResponsableUsuario ug WHERE ug.id.idProceso = :idProceso")
    void eliminarResponsableUsuariosPorProceso(@Param("idProceso") Integer idProceso);
}