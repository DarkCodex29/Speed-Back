package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Accion;
import com.hochschild.speed.back.model.domain.speed.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
