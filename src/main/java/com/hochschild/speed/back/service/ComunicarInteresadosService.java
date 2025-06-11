package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.comunicarInteresados.ComunicarInteresadosBean;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface ComunicarInteresadosService {
    List<InteresadoDTS> getInteresadosIdExpediente(Integer idExpediente, Integer idUsuario);

    List<InteresadoDTS> buscarInteresados(String term);
    List<InteresadoDTS> buscarInteresadosSeguridad(String term);
    ResponseModel comunicarInteresados(ComunicarInteresadosBean comunicarInteresadosBean, Integer idUsuario);
}