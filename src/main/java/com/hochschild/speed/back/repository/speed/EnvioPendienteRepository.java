package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.EnvioPendiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioPendienteRepository extends JpaRepository<EnvioPendiente, Integer> {

    @Query("SELECT e FROM EnvioPendiente e WHERE e.expediente.id=:idExpediente AND e.remitente.id =:idRemitente AND e.estado= 'H'")
    EnvioPendiente obtenerPorExpedienteRemitente(@Param("idExpediente") Integer idExpediente,
                                                 @Param("idRemitente") Integer idRemitente);
}