package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;

public interface RevisarPendienteService {
    DetalleExpedienteBean obtenerDetalleContrato(Integer idExpediente, Integer idUsuario, Integer idPerfil);
}