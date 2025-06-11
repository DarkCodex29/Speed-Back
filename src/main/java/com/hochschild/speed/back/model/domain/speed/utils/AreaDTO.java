package com.hochschild.speed.back.model.domain.speed.utils;

import lombok.Data;

@Data

public class AreaDTO {
    private Integer codigoArea;
    private String descripcionArea;

    public AreaDTO(Integer codigoArea, String descripcionArea) {
        this.codigoArea = codigoArea;
        this.descripcionArea = descripcionArea;
    }
}
