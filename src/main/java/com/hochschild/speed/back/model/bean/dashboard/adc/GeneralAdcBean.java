package com.hochschild.speed.back.model.bean.dashboard.adc;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
public @Data class GeneralAdcBean {

    private String grupo;
    private String estado;
    private Integer cantidad;
    private BigDecimal porcentaje;
}