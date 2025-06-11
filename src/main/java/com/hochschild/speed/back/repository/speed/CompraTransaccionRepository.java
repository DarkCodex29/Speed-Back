package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Accion;
import com.hochschild.speed.back.model.domain.speed.HcCompraTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraTransaccionRepository extends JpaRepository<HcCompraTransaccion, Integer> {

    @Query("SELECT ct FROM HcCompraTransaccion ct WHERE ct.estado = 'A' AND ct.descripcion LIKE '%' + :descripcion + '%'")
    List<HcCompraTransaccion> findByDescripcion(@Param("descripcion") String descripcion);
}
