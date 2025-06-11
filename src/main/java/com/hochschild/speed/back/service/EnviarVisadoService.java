package com.hochschild.speed.back.service;


import com.hochschild.speed.back.model.bean.EnviarVisadoBean;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import com.hochschild.speed.back.model.response.ResponseModel;

import java.util.List;

public interface EnviarVisadoService {

    ResponseModel enviarVisado(EnviarVisadoBean enviarVisadoBean, Integer idUsario);

    ResponseModel validarArchivoPdf(Integer idExpediente);

    List<InteresadoDTS> buscarVisadores(String termino);
}
