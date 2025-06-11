package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.GuardarExpedienteBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.ExploracionBean;
import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.filter.ExpedienteFilter;
import com.hochschild.speed.back.model.filter.reportefilter.ExpedienteAreaFilter;
import com.hochschild.speed.back.model.reporte.ExpedientePorAreaBean;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface ExpedienteService {
    List<Expediente> findAll(ExpedienteFilter expedienteFilter);

    Expediente findById(Integer idProceso);

    ResponseModel guardarExpediente(GuardarExpedienteBean guardarExpedienteBean, Integer idUsuario);

    ResponseModel guardarExploracion(ExploracionBean exploracionBean, Integer idUsuario);

    Integer actualizarExpediente(Expediente expediente, Integer idPuesto);

    Integer guardarExpediente(Expediente expediente, Integer idPuesto);

    List<ExpedientePorAreaBean> filterExpedientesByArea(ExpedienteAreaFilter filter);
}
