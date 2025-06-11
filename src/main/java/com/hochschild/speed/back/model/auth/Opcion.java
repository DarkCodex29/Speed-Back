package com.hochschild.speed.back.model.auth;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author HEEDCOM S.A.C.
 * @since 27/12/2019
 */
@EqualsAndHashCode(callSuper = true)
public @Data class Opcion extends SubOpcion{
    private List<SubOpcion> subOpciones = null;
}
