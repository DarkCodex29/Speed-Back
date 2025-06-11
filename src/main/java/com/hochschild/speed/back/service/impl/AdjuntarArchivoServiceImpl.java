package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.AlfrescoConfig;
import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.AdjuntarArchivoService;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Service("AdjuntarArchivoService")
public class AdjuntarArchivoServiceImpl implements AdjuntarArchivoService {

    private final AlfrescoConfig alfrescoConfig;
    private final CydocConfig cydocConfig;
    private final ExpedienteRepository expedienteRepository;
    private final ArchivoRepository archivoRepository;
    private final VersionRepository versionRepository;
    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final FirmaPorAchivoRepository firmaPorAchivoRepository;
    private final AlfrescoService alfrescoService;
    private static final Logger LOGGER = Logger.getLogger(AdjuntarArchivoServiceImpl.class.getName());

    @Autowired
    public AdjuntarArchivoServiceImpl(
            AlfrescoConfig alfrescoConfig,
            CydocConfig cydocConfig,
            ExpedienteRepository expedienteRepository,
            ArchivoRepository archivoRepository,
            VersionRepository versionRepository,
            UsuarioRepository usuarioRepository,
            DocumentoRepository documentoRepository, FirmaPorAchivoRepository firmaPorAchivoRepository, AlfrescoService alfrescoService) {
        this.alfrescoConfig = alfrescoConfig;
        this.cydocConfig = cydocConfig;
        this.expedienteRepository = expedienteRepository;
        this.archivoRepository = archivoRepository;
        this.versionRepository = versionRepository;
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.firmaPorAchivoRepository = firmaPorAchivoRepository;
        this.alfrescoService = alfrescoService;
    }

    @Override
    public Archivo subirArchivo(Documento documento, String nombreArchivoDisco, Integer fila, Usuario usuario, Map<String, String> map) throws ExcepcionAlfresco {

        Expediente expediente = expedienteRepository.findById(documento.getExpediente().getId());
        String nombreOriginal = nombreArchivoDisco.substring(nombreArchivoDisco.indexOf("] - ") + 4).trim();
        Archivo archivo = archivoRepository.buscarArchivoPorNombreYDocumento(nombreOriginal, documento.getId());

        if (archivo == null) {
            archivo = new Archivo();
            archivo.setNombre(nombreOriginal);
            archivo.setFila(fila);
            LOGGER.info("Nombre archivo disco :" + nombreArchivoDisco);
            archivo.setEstado(Constantes.ESTADO_ACTIVO);
            archivo.setFechaCreacion(new Date());
            archivo.setDocumento(documento);
            LOGGER.info("ruta Local :" + cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivoDisco);
            archivo.setRutaLocal(cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivoDisco);
            archivoRepository.save(archivo);
            archivo.setNuevo(true);
        } else {
            if (archivo.getEstado() != null && archivo.getEstado().equals(Constantes.ESTADO_INACTIVO)) {
                archivo.setEstado(Constantes.ESTADO_ACTIVO);
            }
            archivo.setRutaLocal(cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivoDisco);
            archivoRepository.save(archivo);
        }

        List<Archivo> archivos = new ArrayList<Archivo>();
        archivos.add(archivo);
        List<Documento> documentos = new ArrayList<Documento>();
        documento.setArchivos(archivos);
        documentos.add(documento);
        LOGGER.info("expediente.getEstado(): " + expediente.getEstado());
        LOGGER.info("alfrescoConfig.getHabilitado(): " + alfrescoConfig.getHabilitado());
        LOGGER.info("nombreOriginal: " + nombreOriginal);
        LOGGER.info("expediente.getId(): " + expediente.getId());
        if (expediente.getEstado().equals(Constantes.ESTADO_REGISTRADO) && alfrescoConfig.getHabilitado()) {
            LOGGER.info("ENTRANDO A ALGRESCO AAAAAAAAAAA");//joel
            alfrescoService.subirArchivos(documentos);
            versionarArchivo(documentos, usuario);
        }
        return archivo;
    }
    @Override
    public Archivo subirArchivo(Documento documento, String nombreArchivoDisco, Integer fila, Integer idUsuario, Map<String, String> map) throws ExcepcionAlfresco {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        Expediente expediente = expedienteRepository.findById(documento.getExpediente().getId());
        String nombreOriginal = nombreArchivoDisco.substring(nombreArchivoDisco.indexOf("] - ") + 4).trim();
        Archivo archivo = archivoRepository.buscarArchivoPorNombreYDocumento(nombreOriginal, documento.getId());

        if (archivo == null) {
            archivo = new Archivo();
            archivo.setNombre(nombreOriginal);
            archivo.setFila(fila);
            LOGGER.info("Nombre archivo disco :" + nombreArchivoDisco);
            archivo.setEstado(Constantes.ESTADO_ACTIVO);
            archivo.setFechaCreacion(new Date());
            archivo.setDocumento(documento);
            LOGGER.info("ruta Local :" + cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivoDisco);
            archivo.setRutaLocal(cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivoDisco);
            archivoRepository.save(archivo);
            archivo.setNuevo(true);
        } else {
            if (archivo.getEstado() != null && archivo.getEstado().equals(Constantes.ESTADO_INACTIVO)) {
                archivo.setEstado(Constantes.ESTADO_ACTIVO);
            }
            archivo.setRutaLocal(cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivoDisco);
            archivoRepository.save(archivo);
        }

        List<Archivo> archivos = new ArrayList<Archivo>();
        archivos.add(archivo);
        List<Documento> documentos = new ArrayList<Documento>();
        documento.setArchivos(archivos);
        documentos.add(documento);
        LOGGER.info("expediente.getEstado(): " + expediente.getEstado());
        LOGGER.info("alfrescoConfig.getHabilitado(): " + alfrescoConfig.getHabilitado());
        if (expediente.getEstado().equals(Constantes.ESTADO_REGISTRADO) && alfrescoConfig.getHabilitado()) {
            LOGGER.info("ENTRANDO A ALGRESCO AAAAAAAAAAA");
            alfrescoService.subirArchivos(documentos);
            versionarArchivo(documentos, usuario);
        }
        return archivo;
    }
    @Override
    @Transactional
    public void versionarArchivo(List<Documento> documentos, Usuario usuario) {

        List<Archivo> archivos;
        List<Version> versions;
        Version version;

        if (documentos == null || documentos.isEmpty()) {
            LOGGER.info("Ningun archivo por versionar.");
            return;
        }

        for (Documento documento : documentos) {
            archivos = documento.getArchivos();

            if (archivos == null || archivos.isEmpty()) {
                LOGGER.info("Ningun archivo por versionar.");
                continue;
            }

            for (Archivo archivo : archivos) {
                version = new Version();
                version.setNombre(archivo.getNombre());
                version.setRutaAlfresco(alfrescoService.obtenerUrlAlfresco(archivo));
                version.setAutor(usuario.getUsuario());
                versions = versionRepository.getVersions(archivo.getId());
                version.setNumeroVersion((versions == null || versions.isEmpty()) ? "1.0":getLastNewVersion(versions));
                version.setFechaCreacion(new Date());
                version.setArchivo(archivo);
                versionRepository.save(version);
            }
        }
    }
    @Override
    @Transactional
    public ResponseModel verificarExistenciaArchivo(Integer idDocumento, String nombreArchivo) throws Exception {
        ResponseModel responseModel = new ResponseModel();
        String nombreOriginal = nombreArchivo.substring(nombreArchivo.indexOf("] - ") + 4).trim();
        Documento documento = documentoRepository.findById(idDocumento);
        Archivo archivoEx = archivoRepository.buscarArchivoPorNombreEnExpedienteDocumento(nombreOriginal, documento.getExpediente().getId(), documento.getId());
        Archivo archivo = archivoRepository.buscarArchivoPorNombre(nombreOriginal, idDocumento);
        Expediente expediente = documento.getExpediente();
        if((archivo != null && archivoEx == null) && alfrescoService.existeArchivo(archivo)){
            archivo.setRutaLocal(cydocConfig.getCarpetaArchivosSubidos() + File.separator + nombreArchivo);
            archivo.setEstado(Constantes.ESTADO_ACTIVO);
            archivo = archivoRepository.save(archivo);
            responseModel.setId(archivo.getId());
            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setMessage("SE ACTUALIZA EL NOMBRE DEL ARCHIVO");
            return responseModel;
        }
        if(archivoEx != null){
            if(archivoEx.getEstado().equals(Constantes.ESTADO_INACTIVO)){
                archivoEx.setDocumento(documento);
                archivoEx.setEstado(Constantes.ESTADO_ACTIVO);
                responseModel.setId( archivoEx.getId());
                responseModel.setHttpSatus(HttpStatus.OK);
                return responseModel;
            } else{
                responseModel.setMessage("YA EXISTE ESE ARCHIVO EN EL EXPEDIENTE");
                responseModel.setHttpSatus(HttpStatus.CONFLICT);
                responseModel.setId(-1);
                return responseModel;
            }
        }

        Archivo archivoEnExp = archivoRepository.obtenerPorNombreEnExpediente(nombreOriginal, expediente.getId());
        if(archivoEnExp != null){
            responseModel.setHttpSatus(HttpStatus.CONFLICT);
            responseModel.setMessage("YA EXISTE ESE ARCHIVO EN EL EXPEDIENTE");
            responseModel.setId(-1);
            return responseModel;
        }
        responseModel.setMessage("OK");

        responseModel.setHttpSatus(HttpStatus.OK);
        responseModel.setId(0);
        return responseModel;
    }

    @Override
    @Transactional
    public Archivo subirArchivoAlfrescoSinGuardar(int idDocumento, int idArchivo, Usuario usuario) throws ExcepcionAlfresco{

        Documento documento=documentoRepository.findById(idDocumento);
        Archivo archivo=archivoRepository.findById(idArchivo);
        List<Archivo> archivos=new ArrayList<Archivo>();
        archivos.add(archivo);
        List<Documento> documentos=new ArrayList<Documento>();
        documento.setArchivos(archivos);
        documentos.add(documento);
        LOGGER.info("subirArchivoAlfrescoSinGuardar 1111111");
        alfrescoService.subirArchivos(documentos);
        versionarArchivo(documentos, usuario);
        return archivo;
    }
    @Override
    @Transactional
    public Archivo subirArchivoAlfrescoSinGuardar(int idDocumento, int idArchivo, Integer idUsuario) throws ExcepcionAlfresco{
        Usuario usuario = usuarioRepository.findById(idUsuario);
        Documento documento=documentoRepository.findById(idDocumento);
        Archivo archivo=archivoRepository.findById(idArchivo);
        List<Archivo> archivos=new ArrayList<Archivo>();
        archivos.add(archivo);
        List<Documento> documentos=new ArrayList<Documento>();
        documento.setArchivos(archivos);
        documentos.add(documento);
        LOGGER.info("subirArchivoAlfrescoSinGuardar 222");
        alfrescoService.subirArchivos(documentos);
        versionarArchivo(documentos, usuario);
        return archivo;
    }

    @Override
    public Documento obtenerDocumentoPorId(Integer idDocumento){
        return documentoRepository.findById(idDocumento);
    }
    @Override
    @Transactional
    public Map<String,Object> adjuntarArchivo(Integer idDocumento, String nombre, String usuario, InputStream inputStream){
        Map<String,Object> salida=new HashMap<>();
        int error=Constantes.OK;
        if(alfrescoConfig.getHabilitado()){
            if(idDocumento != null){
                Documento documento=documentoRepository.findById(idDocumento);
                if(documento != null){
                    if(usuario != null){
                        Usuario user=usuarioRepository.obtenerPorUsuario(usuario);
                        if(user != null){
                            Archivo archivo=archivoRepository.obtenerPorNombreEnExpediente(nombre,documento.getExpediente().getId());
                            if(archivo == null){
                                archivo=new Archivo();
                                archivo.setNombre(nombre);
                                archivo.setFechaCreacion(new Date());
                                archivo.setDocumento(documento);
                                archivo.setEstado(Constantes.ESTADO_ACTIVO);
                                archivoRepository.save(archivo);
                                archivo.setNuevo(true);
                            }
                            File file;
                            try{
                                file=File.createTempFile("std",null);
                                Files.copy(inputStream,file.toPath());
                                archivo.setRutaLocal(file.getAbsolutePath());
                                try{
                                    List<Archivo> archivos=new ArrayList<Archivo>();
                                    archivos.add(archivo);
                                    List<Documento> documentos=new ArrayList<Documento>();
                                    documentos.add(documento);
                                    LOGGER.info("adjuntarArchivo 3333");
                                    alfrescoService.subirArchivos(documentos);
                                    versionarArchivo(documentos, user);
                                    salida.put("idArchivo",archivo.getId());
                                }
                                catch(ExcepcionAlfresco e){
                                    LOGGER.error(e.getMessage(),e);
                                    error=Constantes.ERROR_ALFRESCO_SUBIR;
                                }
                            }
                            catch(IOException e){
                                LOGGER.error("Error copiando archivo",e);
                                error=Constantes.ERROR_ALFRESCO_SUBIR;
                            }
                        }
                        else{
                            error=Constantes.ERROR_USUARIO_INEXISTENTE;
                        }
                    }
                    else{
                        error=Constantes.ERROR_USUARIO_INEXISTENTE;
                    }
                }
                else{
                    error=Constantes.ERROR_DOCUMENTO_INEXISTENTE;
                }
            }
            else{
                error=Constantes.ERROR_DOCUMENTO_INEXISTENTE;
            }
        }
        else{
            error=Constantes.ERROR_ALFRESCO_INACTIVO;
        }
        salida.put("error",error);
        return salida;
    }

    @Override
    @Transactional
    public int adjuntarArchivoFirmado(Integer idArchivo, String version, Integer idUsuario, String firmante, InputStream inputStream){
        Archivo archivo=archivoRepository.findById(idArchivo);
        if(archivo != null){
            Usuario usuario=usuarioRepository.findById(idUsuario);
            if(usuario != null){
                FirmaPorArchivo firma=firmaPorAchivoRepository.obtenerPorArchivoVersion(idArchivo,version);
                if(firma == null){
                    int error=(int) adjuntarArchivo(archivo.getDocumento().getId(),archivo.getNombre(),usuario.getUsuario(),inputStream).get("error");
                    if(error == Constantes.OK){
                        firma=new FirmaPorArchivo();
                        firma.setArchivo(archivo);
                        firma.setVersion(version);
                        firma.setUsuario(usuario);
                        firma.setFirmante(firmante);
                        firma.setFechaFirma(new Date());
                        firmaPorAchivoRepository.save(firma);
                        LOGGER.info("Se adjunto el archivo [" + archivo.getNombre() + "(" + version + ")] firmado por " + firmante);
                    }
                    return error;
                }
                return Constantes.ERROR_FIRMA_EXISTENTE;
            }
            return Constantes.ERROR_USUARIO_INEXISTENTE;
        }
        return Constantes.ERROR_ARCHIVO_INEXISTENTE;
    }
    @Override
    public ResponseModel verificarExistenciaArchivoEnExpediente(Integer idExpediente, String nombreArchivo, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();
        try {
            LOGGER.debug("Controlador para verificar la existencia del archivo en todo el expediente");

            if (nombreArchivo != null && nombreArchivo.contains("&")) {
                nombreArchivo = StringEscapeUtils.unescapeHtml(nombreArchivo);
            }

            String nombreOriginal = nombreArchivo.substring(nombreArchivo.indexOf("] - ") + 4).trim();
            Expediente expediente = expedienteRepository.findById(idExpediente);
            LOGGER.info("El expediente encontrado es :" + expediente.getId());

            Archivo archivoEx = archivoRepository.obtenerPorNombreEnExpediente(nombreOriginal, expediente.getId());
            if (archivoEx != null) {
                LOGGER.info("Ya existe un archivo en el expediente con ese nombre, se pide que lo renombren");
                responseModel.setMessage("Ya existe un archivo en el expediente con ese nombre, se pide que lo renombren");
                responseModel.setId(-1);
            } else {
                responseModel.setId(0);
                responseModel.setMessage("No existe archivo");
            }

            responseModel.setHttpSatus(HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Error");
        }

        return responseModel;
    }
    
    @Override
    @Transactional
    public String obtenerUrlAlfresco(Integer idArchivo) { 
    	Archivo archivo = archivoRepository.findById(idArchivo);
        return alfrescoService.obtenerUrlAlfresco(archivo);
    }
    
    @Override
    @Transactional
    public String obtenerIdAlfresco(Integer idArchivo) { 
    	Archivo archivo = archivoRepository.findById(idArchivo);
        return alfrescoService.obtenerIdAlfresco(archivo);
    }
    
    @Override
    @Transactional
    public String getDownloadUrl(Integer idArchivo) { 
    	Archivo archivo = archivoRepository.findById(idArchivo);
        return alfrescoService.getDownloadUrl(archivo);
    }

    private String getLastNewVersion(List<Version> versions) {
        if (versions == null || versions.isEmpty()) {
            return "1.0";
        }

        DecimalFormatSymbols separadores = new DecimalFormatSymbols();
        separadores.setDecimalSeparator('.');
        separadores.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("##.#",separadores);
        df.setRoundingMode(RoundingMode.DOWN);

        return df.format(Double.valueOf(versions.get(0).getNumeroVersion()) + 0.1);
    }
}