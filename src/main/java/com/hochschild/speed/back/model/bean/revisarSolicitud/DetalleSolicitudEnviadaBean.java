package com.hochschild.speed.back.model.bean.revisarSolicitud;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosContratoBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosAdendaBean;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
public @Data class DetalleSolicitudEnviadaBean {

    private DatosAdendaBean datosAdendaBean;
    private DatosContratoBean datosContratoBean;
}