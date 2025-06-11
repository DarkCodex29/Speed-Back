package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.verTraza.HistorialTrazaBean;
import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.Traza;

import java.util.Date;
import java.util.List;

public interface VerTrazaService {

    Date obtenerFechaLimite(Expediente expediente);

    HistorialTrazaBean obtenerHistoriaTrazasPorExpediente(Integer idExpediente);

    List<Traza> obtenerPorExpediente(int idExpediente);

    Rol obtenerRol(Integer id);
}
