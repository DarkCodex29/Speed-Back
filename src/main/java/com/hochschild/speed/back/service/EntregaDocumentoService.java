package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.utils.EntregaDocumentoDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.transaction.annotation.Transactional;

public interface EntregaDocumentoService {
    @Transactional
    ResponseModel registrarEntrega(EntregaDocumentoDTO entregaDocumentoDTO, Integer idUsuario);
}
