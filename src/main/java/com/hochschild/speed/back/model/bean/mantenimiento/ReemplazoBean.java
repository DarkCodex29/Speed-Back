package com.hochschild.speed.back.model.bean.mantenimiento;

import lombok.Data;
import java.util.Date;
import java.util.List;

public @Data class ReemplazoBean {
    private Integer id;
    private Date fechaDesde;
    private Date fechaHasta;
    private Integer idReemplazado;
    private Integer idReemplazante;
    private List<ProcesoReeemplazoBean> procesos;
}