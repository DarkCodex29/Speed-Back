package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.Proceso;

import java.util.Date;

public interface GridService {

    Date calcularFechaFinconPlazo(Expediente e, Proceso p);
}
