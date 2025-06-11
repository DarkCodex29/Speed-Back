package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.ServicioDTS;
import com.hochschild.speed.back.model.filter.reportefilter.ServicioFilter;

import java.util.List;

public interface ServicioDTSService {
    List<ServicioDTS> filtrarServicios(ServicioFilter servicioFilter);
}
