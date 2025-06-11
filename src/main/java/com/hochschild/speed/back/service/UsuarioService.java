package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Usuario;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    Usuario dialogSeleccionarUsuarios(Integer idUsuario);

    Usuario findUsuarioById(Integer idUsuario);
}
