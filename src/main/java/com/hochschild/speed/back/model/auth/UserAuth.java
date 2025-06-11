package com.hochschild.speed.back.model.auth;

import lombok.Data;

/**
 *
 * @author HEEDCOM S.A.C.
 * @since 27/12/2019
 */
public @Data class UserAuth {

    private Integer idPerfil;
    private String username;
    private String password;
}
