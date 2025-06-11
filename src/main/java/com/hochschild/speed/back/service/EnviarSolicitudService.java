package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.response.ResponseModel;

public interface EnviarSolicitudService {
    ResponseModel enviarSolicitud(Integer idExpediente, Integer idUsuario);
}
