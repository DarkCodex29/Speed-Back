package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.revisarSolicitud.DetalleSolicitudEnviadaBean;
import com.hochschild.speed.back.model.bean.revisarSolicitud.SolicitudEnviadaBean;

public interface RevisarSolicitudEnviadaService {
    DetalleSolicitudEnviadaBean revisarSolicitudesPorEnviar(SolicitudEnviadaBean solicitudEnviadaBean, Integer idUsuario);
}