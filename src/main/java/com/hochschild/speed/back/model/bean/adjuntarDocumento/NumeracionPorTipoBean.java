package com.hochschild.speed.back.model.bean.adjuntarDocumento;

import com.hochschild.speed.back.model.response.ResponseModel;
import lombok.Data;

public @Data class NumeracionPorTipoBean extends ResponseModel {
    private String tipo;
    private String valor;
}
