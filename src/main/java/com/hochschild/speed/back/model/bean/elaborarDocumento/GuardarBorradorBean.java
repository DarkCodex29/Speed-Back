package com.hochschild.speed.back.model.bean.elaborarDocumento;

import lombok.Data;

import java.util.List;

public @Data class GuardarBorradorBean {
    private Integer id;
    private String archivo;
    private Integer[] idDestinatarios;
    private Boolean enviadoC;
}
