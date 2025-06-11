package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TipoCampo;
import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoCampoRepository extends JpaRepository<TipoCampo, Integer> {
    @Query("SELECT e FROM TipoCampo e WHERE e.id = :idTipoCampo")
    TipoCampo findById(@Param("idTipoCampo") Integer idTipoCampo);

    @Query("SELECT t FROM TipoCampo t WHERE t.estado = 'A'")
    List<TipoCampo> obtenerActivos();
}