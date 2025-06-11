package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.HcMovimientoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HcMovimientoTransaccionRepository extends JpaRepository<HcMovimientoTransaccion, Integer> {

    HcMovimientoTransaccion findByCodigoRegistro(Integer codigoRegistro);
}
