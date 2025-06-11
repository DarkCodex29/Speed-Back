package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.UsuarioEnvioPendiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioEnvioPendienteRepository extends JpaRepository<UsuarioEnvioPendiente, Integer> {
    @Query("SELECT uep FROM UsuarioEnvioPendiente uep WHERE uep.id.envioPendiente.id =:idEnvioPendiente")
    List<UsuarioEnvioPendiente> obtenerPorEnvioPendiente(@Param("idEnvioPendiente") Integer idEnvioPendiente);
}