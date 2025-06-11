package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoClienteRepository extends JpaRepository<TipoCliente, Integer> {
    @Query("SELECT e FROM TipoCliente e WHERE e.id = :idTipoCliente")
    TipoCliente findById(@Param("idTipoCliente") Integer idTipoCliente);

    @Query("SELECT tc FROM TipoCliente tc WHERE tc.estado = :estado")
    List<TipoCliente> obtenerTipoClientePorEstado(@Param("estado") String estado);
}