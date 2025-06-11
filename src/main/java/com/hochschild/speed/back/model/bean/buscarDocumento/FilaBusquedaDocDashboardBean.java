package com.hochschild.speed.back.model.bean.buscarDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class FilaBusquedaDocDashboardBean {
    private Integer idDocumentoLegal;
    private String numeroDocumento;
    private String area;
    private String ubicacion;
    private String contraparte;
    private String fechaSolicitud;
    private String proceso;

    public FilaBusquedaDocDashboardBean() {}
}
