package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.BotonPorGridPorPerfil;
import com.hochschild.speed.back.model.domain.speed.BotonPorGridPorPerfilPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BotonPorGridPorPerfilRepository extends JpaRepository<BotonPorGridPorPerfil, BotonPorGridPorPerfilPK> {
    @Query("SELECT ad FROM BotonPorGridPorPerfil ad WHERE ad.id.boton.id = :idBoton")
    List<BotonPorGridPorPerfil> findBotonesById(@Param("idBoton") Integer idBoton);
    @Modifying
    @Query("DELETE FROM BotonPorGridPorPerfil ug WHERE ug.id.boton.id = :idBoton")
    void eliminarBotonesPorGridPorPerfil(@Param("idBoton") Integer idBoton);
}