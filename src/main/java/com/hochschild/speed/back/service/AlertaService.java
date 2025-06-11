package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.Alerta;
import com.hochschild.speed.back.model.domain.speed.Traza;

public interface AlertaService {
    Alerta obtenerAlerta(Expediente expediente, Traza traza, Integer idGrid);
}
