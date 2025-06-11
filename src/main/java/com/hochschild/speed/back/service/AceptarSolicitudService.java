package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.response.ResponseModel;

public interface AceptarSolicitudService {
    ResponseModel aceptarSolicitud(Integer idExpediente, Integer idUsuario);
}
