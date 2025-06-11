package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosAdendaBean;
import com.hochschild.speed.back.model.filter.bandejaEntrada.AdendaFilter;

public interface DatosAdendaService {
    DatosAdendaBean botonDatosAdenda(AdendaFilter adendaBean, Integer idUsuario);
}
