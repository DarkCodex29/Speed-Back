package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.domain.speed.utils.EstadoDocumentoDTO;

import java.util.List;

public interface DocumentoDao {
    List<EstadoDocumentoDTO> listarEstadosDocumento(String codigoDocumento);
}
