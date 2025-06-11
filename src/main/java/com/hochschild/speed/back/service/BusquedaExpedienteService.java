package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.Documento;

import java.util.List;

public interface BusquedaExpedienteService {

    List<Documento> buscarPorExpediente(Integer idExpediente);

    List<Archivo> obtenerArchivosxDocumento(Documento documento);
}
