package com.hochschild.speed.back.model.bean.dashboard.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public @Data class GeneralAreaBean {

    private String proceso;
    private String estado;
    private Integer cantidad;
    private BigDecimal porcentaje;
}