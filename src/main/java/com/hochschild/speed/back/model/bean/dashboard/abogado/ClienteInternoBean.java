package com.hochschild.speed.back.model.bean.dashboard.abogado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ClienteInternoBean {

    private String area;
    private String abogado;
    private Integer cantidad;
    private BigDecimal porcentaje;
}