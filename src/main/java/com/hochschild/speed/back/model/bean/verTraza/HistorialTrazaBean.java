package com.hochschild.speed.back.model.bean.verTraza;

import com.hochschild.speed.back.model.response.ResponseModel;
import lombok.Data;

import java.util.List;

public @Data class HistorialTrazaBean extends ResponseModel {
    private String numero;
    private String fechaLimite;
    private List<TrazaBean> historial;
}
