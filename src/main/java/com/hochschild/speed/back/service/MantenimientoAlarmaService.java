package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.bandejaEntrada.AlarmaBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.BandejaAlarmaBean;
import com.hochschild.speed.back.model.domain.speed.HcAlarma;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface MantenimientoAlarmaService {
    BandejaAlarmaBean bandejaAlarma(Integer idUsuario, Integer idExpediente);

    List<AlarmaBean> obtenerAlarmasBean(Integer idDocumentoLegal);

    ResponseModel guardarAlarma(AlarmaBean alarmaBean);

    List<HcAlarma> obtenerAlarmas(Integer idDocumentoLegal);

    void guardarAlarmaVisados(HcAlarma alarma, Integer[] idVisadores, Integer[] esGrupo);

    ResponseModel eliminarAlarma(Integer idUsuario, Integer idAlarma);
}
