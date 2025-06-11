package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.UsuarioAccesoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioAccesoSolicitudRepository extends JpaRepository<UsuarioAccesoSolicitud, Integer> {
    @Query("SELECT e FROM UsuarioAccesoSolicitud e WHERE e.id = :idUsuarioAccesoSolicitud")
    UsuarioAccesoSolicitud findById(@Param("idUsuarioAccesoSolicitud") Integer idUsuarioAccesoSolicitud);

    @Query("SELECT u FROM UsuarioAccesoSolicitud u WHERE u.estado = :estado")
    List<UsuarioAccesoSolicitud> getUsuariosConAcceso(@Param("estado") String estado);
}