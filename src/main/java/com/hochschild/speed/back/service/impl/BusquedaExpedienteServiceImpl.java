package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.repository.speed.ArchivoRepository;
import com.hochschild.speed.back.repository.speed.DocumentoRepository;
import com.hochschild.speed.back.service.BusquedaExpedienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("busquedaExpedienteService")
public class BusquedaExpedienteServiceImpl implements BusquedaExpedienteService {

    private final DocumentoRepository documentoRepository;
    private final ArchivoRepository archivoRepository;

    @Autowired
    public BusquedaExpedienteServiceImpl(DocumentoRepository documentoRepository,
                                         ArchivoRepository archivoRepository) {
        this.documentoRepository = documentoRepository;
        this.archivoRepository = archivoRepository;
    }

    @Override
    public List<Documento> buscarPorExpediente(Integer idExpediente) {
        return documentoRepository.obtenerPorExpediente(idExpediente);
    }

    @Override
    public List<Archivo> obtenerArchivosxDocumento(Documento documento) {
        return archivoRepository.obtenerPorDocumento(documento.getId());
    }
}
