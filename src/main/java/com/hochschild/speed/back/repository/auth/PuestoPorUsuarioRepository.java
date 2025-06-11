package com.hochschild.speed.back.repository.auth;

import com.hochschild.speed.back.model.domain.auth.PuestoPorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuestoPorUsuarioRepository extends JpaRepository<PuestoPorUsuario, Long> {

    List<PuestoPorUsuario> findByIdPersonal(String idPersonal);

    List<PuestoPorUsuario> findByIdUsuarioAndActivo(String idUsuario, String activo);

    List<PuestoPorUsuario> findByIdUsuario(String idUsuario);

}