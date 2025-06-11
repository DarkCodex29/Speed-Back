package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Usuario;

public interface LoginService {
    Usuario login(String usuario, String clave, Integer idPerfil);
}