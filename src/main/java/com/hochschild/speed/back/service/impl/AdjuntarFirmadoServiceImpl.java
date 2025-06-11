package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.repository.speed.DocumentoRepository;
import com.hochschild.speed.back.repository.speed.HcAlarmaRepository;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.AdjuntarArchivoService;
import com.hochschild.speed.back.service.AdjuntarFirmadoService;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AdjuntarFirmadoServiceImpl implements AdjuntarFirmadoService {
    private final AdjuntarArchivoService adjuntarArchivoService;

    private final CommonBusinessLogicService commonService;

    private final DocumentoRepository documentoRepository;
    private final CydocConfig cydocConfig;

    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    private final HcAlarmaRepository alarmaRepository;
    private final UsuarioRepository usuarioRepository;

    public AdjuntarFirmadoServiceImpl(AdjuntarArchivoService adjuntarArchivoService, CommonBusinessLogicService commonService, DocumentoRepository documentoRepository, CydocConfig cydocConfig, HcDocumentoLegalRepository hcDocumentoLegalRepository, HcAlarmaRepository alarmaRepository, UsuarioRepository usuarioRepository) {
        this.adjuntarArchivoService = adjuntarArchivoService;
        this.commonService = commonService;
        this.documentoRepository = documentoRepository;
        this.cydocConfig = cydocConfig;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.alarmaRepository = alarmaRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    public ResponseModelFile uploadFile(MultipartFile archivoSubir, Integer idUsuario){
        ResponseModelFile response = new ResponseModelFile();
        String resultado = "error";
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if(usuario != null) {
            if (archivoSubir.getSize() > 0) {
                int numeroGenerado = new Random().nextInt(999999999);
                String archivo = archivoSubir.getOriginalFilename();
                String nombreFormateado = AppUtil.quitarAcentos(archivo);
                String nombreSubir = "[" + numeroGenerado + "] - " + nombreFormateado;
                try {
                    InputStream inputStream = archivoSubir.getInputStream();
                    String fileName = cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreSubir;
                    OutputStream outputStream = new FileOutputStream(fileName);
                    int readBytes = 0;
                    byte[] buffer = new byte[10000];
                    while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                        outputStream.write(buffer, 0, readBytes);
                    }
                    outputStream.close();
                    inputStream.close();
                    response.setRandom(String.valueOf(numeroGenerado));
                    response.setArchivo(nombreFormateado);
                    response.setNombreArchivoDisco(nombreSubir);
                    response.setMessage("Exito");
                    response.setHttpSatus(HttpStatus.CREATED);
                    return response;
                } catch (IOException e) {
                    response.setMessage("Ocurri\u00f3 un error subiendo el archivo");
                    response.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    return response;
                }
            }
            response.setMessage("Sin adjuntos");
            response.setHttpSatus(HttpStatus.NOT_FOUND);
            return response;
        }
        response.setMessage("Sin adjuntos");
        response.setHttpSatus(HttpStatus.NOT_FOUND);
        return response;
    }
    @Override
    @Transactional
    public ResponseModel guardarFirmado(Integer idDocumento, String archivo, Integer idUsuario) {
        ResponseModel response = new ResponseModel();
        Integer idArchivo = 0;
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (archivo != null) {
            Archivo archivoSubir = null;
            Documento documento = documentoRepository.findById(idDocumento);
            try {
                archivoSubir = adjuntarArchivoService.subirArchivo(documento, archivo, null, usuario, null);
                idArchivo = archivoSubir.getId();
            } catch (ExcepcionAlfresco e) {
                response.setId(null);
                response.setMessage("No se pudo subir el archivo. Verifique el nombre.");
                response.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return response;
            }
            if (idArchivo > 0) {
                HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.findById(documento.getExpediente().getDocumentoLegal().getId());
                documentoLegal.setEstado(Constantes.ESTADO_HC_COMUNICACION);
                hcDocumentoLegalRepository.save(documentoLegal);

                commonService.generarTraza(documento.getExpediente().getId(), usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_DOCUMENTO_FIRMADO, Constantes.ACCION_ADJUNTAR_FIRMADO);

                if (documentoLegal.getContrato() != null) {
                    List<HcAlarma> alarmasActuales = alarmaRepository.obtenerAlarmas(documentoLegal.getId());

                    if (alarmasActuales == null || alarmasActuales.isEmpty()) {
                        if (!documentoLegal.getContrato().getIndefinido() && !documentoLegal.getContrato().getRenovacion_auto() && documentoLegal.getContrato().getFechaFin().after(new Date())) {
                            //Crear la alarma por defecto
                            commonService.crearAlarma(documentoLegal, documentoLegal.getContrato().getFechaFin());
                        }
                    }
                } else if (documentoLegal.getAdenda() != null) {
                    HcAdenda adenda = documentoLegal.getAdenda();
                    HcDocumentoLegal contrato = hcDocumentoLegalRepository.findById(adenda.getContrato().getId());

                    if (adenda.getModifica_fin()) {
                        commonService.desactivarAlarmas(contrato);

                        if (!adenda.getIndefinido() && adenda.getNuevaFechaFin().after(new Date())) {
                            commonService.crearAlarma(contrato, adenda.getNuevaFechaFin());
                        }
                    }
                }
                response.setHttpSatus(HttpStatus.OK);
                response.setId(idDocumento);
                response.setMessage("Éxito en la operacion");
                return response;
            }
        }
        response.setId(null);
        response.setMessage("No se pudo subir el archivo. Verifique el nombre.");
        response.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }
}
