package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Busqueda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusquedaRepository extends JpaRepository<Busqueda, Integer> {
    @Query("SELECT b FROM Busqueda b WHERE b.usuario.id=:idUsuario and b.estado='A'")
    List<Busqueda> listarBusquedasGuardadas(@Param("idUsuario") Integer idUsuario);
}