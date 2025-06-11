package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.filter.ProcesoFilter;

import java.util.List;

public interface ProcesoService {
    List<Proceso> find(ProcesoFilter filter);

    Proceso findById(Integer idProceso);
}