package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.response.ResponseModel;

public interface EnviarFirmaService {

    ResponseModel enviarFirma(Integer idExpediente, Integer idUsuario);
}
