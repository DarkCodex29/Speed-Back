package com.hochschild.speed.back.repository.auth;

import com.hochschild.speed.back.model.domain.auth.PuestoPorUsuarioExterno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuestoPorUsuarioExternoRepository extends JpaRepository<PuestoPorUsuarioExterno, Long> {

    List<PuestoPorUsuarioExterno>  findByIdUsuario(String idUsuario);
}
