package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.TabModelBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.model.response.FilterTipoComponent;
import org.springframework.http.ResponseEntity;

public interface RevisarTrazaService {
    ResponseEntity<FilterTipoComponent> filterExpedienteByStatus(Integer idExpediente, Integer idUsuario, Integer idPerfil);

    DetalleExpedienteBean revisarExpedienteById(Integer idExpediente, Integer idUsuario, Integer idPerfil);

    DetalleExpedienteBean obtenerDetalleExpedientePorTraza(Integer idTraza, Integer idUsuario, Integer idPerfil);
    DetalleExpedienteBean obtenerDetalleExpediente(Integer idExpediente, Integer idUsuario);

    TabModelBean obtenerDatosBasicosExpediente(Integer idTraza);
}