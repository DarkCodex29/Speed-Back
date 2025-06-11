package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
import java.util.List;

public @Data class TipoDocumentoBean {

    private Integer id;
    private String descripcion;
    private Boolean estado;
    private String fechaCreacion;
    private Boolean firmable;
    private String nombre;
    private List<CampoDocumentoBean> campos;
}