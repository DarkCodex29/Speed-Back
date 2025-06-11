package com.hochschild.speed.back.model.bean.dashboard.abogado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class FilterAbogadoBean {

    private Integer tipo;
    private String usuario;
}