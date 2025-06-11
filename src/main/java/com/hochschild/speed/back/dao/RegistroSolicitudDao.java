package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.registrarSolicitud.ContratoAdendaBean;
import java.util.List;

public interface RegistroSolicitudDao {
    List<ContratoAdendaBean> getContratoByAdenda(String numeroSumilla);
}