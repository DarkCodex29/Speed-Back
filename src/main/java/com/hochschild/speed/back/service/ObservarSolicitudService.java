package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.ObservarSolicitudBean;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.Map;

public interface ObservarSolicitudService {
    ResponseModel observarSolicitud(ObservarSolicitudBean observarSolicitudBean, Integer idUsuario);

    Map<String, Object> obtenerDestinatario(Integer idExpediente);
}
