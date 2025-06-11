package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;

public interface RevisarSolicitudService {
    DetalleExpedienteBean obtenerDetalleContrato(Integer idExpediente, Integer idUsuario, Integer idPerfil);

    DetalleExpedienteBean obtenerDetalleContrato2(Integer idExpediente, Integer idUsuario, Integer idPerfil);
}