package com.hochschild.speed.back.model.bean.dashboard.abogado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public @Data class GeneralBean {

    private String proceso;
    private String estado;
    private Integer cantidad;
    private BigDecimal porcentaje;
    private String color;
}