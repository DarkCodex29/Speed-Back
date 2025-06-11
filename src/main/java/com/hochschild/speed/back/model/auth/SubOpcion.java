package com.hochschild.speed.back.model.auth;

import java.util.List;
import lombok.Data;

/**
 *
 * @author HEEDCOM S.A.C.
 * @since 27/12/2019
 */
public @Data class SubOpcion {

    private Long id;
    private Long idPadre;
    private String nombre;
    private String linkOpcion;
    private List<SubOpcion> subOpciones;
}
