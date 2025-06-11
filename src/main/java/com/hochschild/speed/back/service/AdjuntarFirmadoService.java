package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface AdjuntarFirmadoService {

    ResponseModelFile uploadFile(MultipartFile archivoSubir, Integer idUsuario);

    @Transactional
    ResponseModel guardarFirmado(Integer idDocumento, String archivo, Integer idUsuario);
}
