package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.auth.User;

/**
 * @author HEEDCOM S.A.C.
 * @since 02/05/2020
 */

public interface CustomUserService {
    User getUserByUsername(String username);

    User getUserByUsernameWithMenu(String idUser, Integer idPerfil);

    User getUserByUsernameWithMenuMobile(String username, Integer idPerfil);
}