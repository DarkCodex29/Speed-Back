package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    @Query("SELECT d FROM Direccion d WHERE d.cliente.id = :idCliente")
    List<Direccion> obtenerDireccionesPorCliente(@Param("idCliente") Integer idCliente);
}
