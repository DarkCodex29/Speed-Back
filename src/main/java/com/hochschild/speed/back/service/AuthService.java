package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.auth.User;
import com.hochschild.speed.back.model.domain.speed.Usuario;

import java.util.Map;

public interface AuthService {
    User login(String username, String password, Integer idPerfil);

    User loginExternal(String key);

    User loginExternalMobile(String key);

    String generateExternalLink(String usuario, Integer idTraza);
}