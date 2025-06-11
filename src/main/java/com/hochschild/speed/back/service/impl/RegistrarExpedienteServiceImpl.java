package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.NotificacionExterna;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import com.hochschild.speed.back.util.exception.ExcepcionBusinessAdendaAutomatica;
import com.hochschild.speed.back.util.exception.ExcepcionNoPlantilla;
import com.hochschild.speed.back.ws.remote.doc_api.client.DocClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@Service("RegistrarExpedienteService")
public class RegistrarExpedienteServiceImpl implements RegistrarExpedienteService {
    private static final Logger LOGGER = Logger.getLogger(RegistrarExpedienteServiceImpl.class.getName());
    private final DocClient docClient;
    private final AlfrescoService alfrescoService;
    private final AdjuntarArchivoService adjuntarArchivoService;
    private final ElaborarDocumentoService elaborarDocumentoService;
    private final EnviarNotificacionService enviarNotificacionService;
    private final GenerarHcDocumentoFromPlantillaService generarHcDocumentoFromPlantillaService;
    private final FirmaElectronicaService firmaElectronicaService;
    private final NotificacionesConfig notificacionesConfig;
    private final CydocConfig cydocConfig;
    private final UsuarioRepository usuarioRepository;
    private final ExpedienteRepository expedienteRepository;
    private final DocumentoRepository documentoRepository;
    private final ArchivoRepository archivoRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final TrazaRepository trazaRepository;
    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;
    private final WorkflowRepository workflowRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final PasoWorkflowRepository pasoWorkflowRepository;
    private final ParametroRepository parametroRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepresentanteCompaniaRepository usuarioRepresentanteCompaniaRepository;
    private final HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository;
    private final HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository;
    private final ProcesoRepository procesoRepository;

    @Autowired
    public RegistrarExpedienteServiceImpl(DocClient docClient,
                                          AlfrescoService alfrescoService,
                                          AdjuntarArchivoService adjuntarArchivoService,
                                          ElaborarDocumentoService elaborarDocumentoService,
                                          EnviarNotificacionService enviarNotificacionService,
                                          GenerarHcDocumentoFromPlantillaService generarHcDocumentoFromPlantillaService,
                                          FirmaElectronicaService firmaElectronicaService, NotificacionesConfig notificacionesConfig,
                                          CydocConfig cydocConfig,
                                          UsuarioRepository usuarioRepository,
                                          ExpedienteRepository expedienteRepository,
                                          DocumentoRepository documentoRepository,
                                          ArchivoRepository archivoRepository,
                                          HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                          TrazaRepository trazaRepository,
                                          UsuarioPorTrazaRepository usuarioPorTrazaRepository,
                                          WorkflowRepository workflowRepository,
                                          TipoNotificacionRepository tipoNotificacionRepository,
                                          PasoWorkflowRepository pasoWorkflowRepository,
                                          ParametroRepository parametroRepository,
                                          PerfilRepository perfilRepository,
                                          UsuarioRepresentanteCompaniaRepository usuarioRepresentanteCompaniaRepository,
                                          HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository,
                                          HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository,
                                          ProcesoRepository procesoRepository) {
        this.docClient = docClient;
        this.alfrescoService = alfrescoService;
        this.adjuntarArchivoService = adjuntarArchivoService;
        this.elaborarDocumentoService = elaborarDocumentoService;
        this.enviarNotificacionService = enviarNotificacionService;
        this.generarHcDocumentoFromPlantillaService = generarHcDocumentoFromPlantillaService;
        this.firmaElectronicaService = firmaElectronicaService;
        this.notificacionesConfig = notificacionesConfig;
        this.cydocConfig = cydocConfig;
        this.usuarioRepository = usuarioRepository;
        this.expedienteRepository = expedienteRepository;
        this.documentoRepository = documentoRepository;
        this.archivoRepository = archivoRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.trazaRepository = trazaRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.workflowRepository = workflowRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.pasoWorkflowRepository = pasoWorkflowRepository;
        this.parametroRepository = parametroRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioRepresentanteCompaniaRepository = usuarioRepresentanteCompaniaRepository;
        this.hcTipoContratoConfiguracionRepository = hcTipoContratoConfiguracionRepository;
        this.procesoRepository = procesoRepository;
        this.hcRepresentantePorDocumentoRepository = hcRepresentantePorDocumentoRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Integer registrarExpedienteSolicitudHC(Usuario usuario, Integer idExpediente, Integer idResponsable, Boolean notificar) throws RuntimeException {

        Expediente expediente = obtenerExpedienteNuevo(idExpediente);

        if (expediente != null) {

            Usuario usuarioResponsable = null;

            if (idResponsable != null) {
                usuarioResponsable = usuarioRepository.findById(idResponsable);
            }
            if (usuarioResponsable != null) {
                try {
                    //AAA
                    Map<String, Object> res = guardarTrazaExpediente(usuario, expediente, usuarioResponsable, Constantes.OBS_HC_ENVIO_SOLICITUD);
                    if (!(Boolean) res.get("trazaExistente")) {

                        subirArchivosAlfresco(expediente, usuario);///ALFRESCO
                        HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());

                        documentoLegal.setFechaSolicitud(new Date());

                        if (documentoLegal.getAdenda() != null) {
                            LOGGER.info("Estado doc:" + documentoLegal.getEstado());
                            //Trabajando con adendas.
                            if (documentoLegal.getAdenda().getHcTipoContrato() == null) {
                                LOGGER.info("RegistrarExpedienteSolicitudHC - Adenda");
                                documentoLegal.setEstado(Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
                            }
                        } else {
                            //Trabajando con contratos.
                            LOGGER.info("RegistrarExpedienteSolicitudHC - Contratos");
                            documentoLegal.setEstado(Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
                        }

                        LOGGER.info("--------------Documento Legal Id - registrarExpedienteSolicitudHC-------------");
                        LOGGER.info(documentoLegal.getEstado());
                        LOGGER.info(documentoLegal.getId());
                        LOGGER.info("------------------------------------------------------------------------------");
                        hcDocumentoLegalRepository.save(documentoLegal);

                        if (notificar) {

                            TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getRegistroSolicitud());
                            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());

                            List<Usuario> destinatarios = new ArrayList<>();
                            destinatarios.add(hcDocumentoLegal.getResponsable());
                            if (!Objects.equals(hcDocumentoLegal.getResponsable().getId(), hcDocumentoLegal.getSolicitante().getId())) {
                                destinatarios.add(hcDocumentoLegal.getSolicitante());
                            }

                            if (documentoLegal.getAdenda() != null && documentoLegal.getAdenda().getHcTipoContrato() != null) {
                                LOGGER.info("ES ADENDA AUTOMATICA");
                                LOGGER.info("TipoNotificacion = " + tipoNotificacion.getNombre());

                                if (documentoLegal.getAdenda().getHcTipoContrato().getId() == Constantes.TIPO_DOCUMENTO_COD_ADENDA_RESOLUCION_UNILATERAL) {

                                    enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) res.get("traza"));

                                    Documento documentoSolicitud = documentoRepository.findFirstByExpedienteIdAndTipoDocumentoIdOrderByFechaCreacionAsc(documentoLegal.getExpediente().getId(), Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_SOLICITUD));
                                    List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documentoSolicitud.getId());
                                    List<File> files = new ArrayList<>();

                                    for (Archivo archivo : archivos) {
                                        String rutaLocal = archivo.getRutaLocal();
                                        File file = new File(rutaLocal);
                                        if (file.exists()) {
                                            files.add(file);
                                        } else {
                                            LOGGER.info("El archivo en la ruta " + rutaLocal + " no existe.");
                                        }
                                    }

                                    for (File file : files) {
                                        LOGGER.info("Archivo cargado: " + file.getAbsolutePath());
                                    }

                                    //destinatarios externos
                                    List<Cliente> clientes = hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(documentoLegal.getId());


                                    NotificacionExterna notificacionExterna = new NotificacionExterna();
                                    notificacionExterna.setNumeroSolicitud(hcDocumentoLegal.getNumero());
                                    notificacionExterna.setTipoProceso("Adenda Automática");
                                    notificacionExterna.setSumilla(hcDocumentoLegal.getSumilla());
                                    notificacionExterna.setCompania(Constantes.NOMBRE_COMPANY);
                                    notificacionExterna.setCompania(hcDocumentoLegal.getArea().getCompania().getNombre());
                                    notificacionExterna.setArchivos(files);
                                    notificacionExterna.setRazonSocialContraparte(documentoLegal.getContraparte().getRazonSocial());
                                    notificacionExterna.setDescripcionContrato(hcDocumentoLegal.getAdenda().getContrato().getDescripcion());
                                    for (Cliente cliente : clientes) {
                                        if (cliente.getCorreo()
                                                == null || cliente.getCorreo().trim().isEmpty()) {
                                            LOGGER.info("El representante legal " + cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno() + " no tiene correo.");
                                        }else{
                                            List<String> destinatariosExternos = new ArrayList<>();
                                            destinatariosExternos.add(cliente.getCorreo());
                                            notificacionExterna.setDestinatarios(destinatariosExternos);
                                            if(cliente.getRazonSocial()!=null){
                                                notificacionExterna.setContraparte(cliente.getRazonSocial());
                                            }else{
                                                notificacionExterna.setContraparte(cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno() + " " + cliente.getNombre());
                                            }

                                            enviarNotificacionService.enviarNotificacionExterna(notificacionExterna);
                                            LOGGER.info("Se envio correo al representante legal " + cliente.getNombre()
                                                    + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno() + " ("+cliente.getCorreo()+").");
                                        }
                                    }
                                }
                            } else {
                                LOGGER.info("NO ES ADENDA AUTOMATICA");
                                LOGGER.info("TipoNotificacion = " + tipoNotificacion.getNombre());
                                enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) res.get("traza"));
                            }
                        }

                        //Merge Process
                        if (documentoLegal.getAdenda() != null && documentoLegal.getAdenda().getHcTipoContrato() != null) {

                            Integer idHcTipoContrato = documentoLegal.getAdenda().getHcTipoContrato().getId();
                            HcTipoContratoConfiguracion hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegal.getAdenda().getHcTipoContrato().getId());

                            if (hcTipoContratoConfiguracion.getEstadoSolicitud().equals(Constantes.ESTADO_HC_ENVIADO_FIRMA.toString())) {

                                Boolean result;
                                result = generarHcDocumentoFromPlantillaService.validarPlantilla(documentoLegal, idHcTipoContrato);

                                if (!result) {
                                    throw new ExcepcionNoPlantilla("No hay plantilla");
                                } else {

                                    Map<String, String> resultUploadFilePdf = generarPlantillaPdf(documentoLegal, idHcTipoContrato, usuario);

                                    if (resultUploadFilePdf.get("resultado").equals("exito")) {

                                        Documento documentoAdenda = documentoRepository.obtenerDocumentoPorExpedienteYTipoDocumento(documentoLegal.getExpediente().getId(), Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_ADENDA));
                                        Documento documentoSolicitud = documentoRepository.obtenerDocumentoPorExpedienteYTipoDocumento(documentoLegal.getExpediente().getId(), Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_SOLICITUD));
                                        List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documentoSolicitud.getId());

                                        LOGGER.info("Es documento unido = " + hcTipoContratoConfiguracion.getEsDocumentoUnido());
                                        LOGGER.info("Documento Solicitud =" + documentoSolicitud.getId());
                                        LOGGER.info("Cnt de archivos " + archivos.size());

                                        if (hcTipoContratoConfiguracion.getEsDocumentoUnido().equals(Constantes.ESTADO_ACTIVO_UNIR_DOC) && !archivos.isEmpty()) {

                                            LOGGER.info("ARCHIVO  getEsDocumentoUnido ");
                                            if (documentoSolicitud.getArchivos().get(0).getVersions().get(0).getRutaAlfresco() != null) {
                                                LOGGER.info("ARCHIVO getArchivos().get(0).getVersions().get(0).getRutaAlfresco() = " + documentoSolicitud.getArchivos().get(0).getVersions().get(0).getRutaAlfresco());
                                            }
                                            File fileArchivoAdjunto = null;

                                            if (documentoSolicitud.getArchivos().get(0).getVersions().get(0).getRutaAlfresco() != null) {
                                                Map<String, Object> resultAlfresco = alfrescoService.obtenerArchivo(documentoSolicitud.getArchivos().get(0), null);
                                                if (resultAlfresco != null) {
                                                    InputStream in = (InputStream) resultAlfresco.get("stream");

                                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                                    String archivoAdjuntoFileName = timestamp.getTime() + "_DOCUMENTO_OTRO" + documentoSolicitud.getArchivos().get(0).getNombre().split("//.")[0];
                                                    String archivoAdjuntoFilePath = cydocConfig.getCarpetaArchivosSubidos() + File.separator + archivoAdjuntoFileName;

                                                    LOGGER.info("/ " + archivoAdjuntoFileName);
                                                    LOGGER.info("/ " + archivoAdjuntoFilePath);

                                                    fileArchivoAdjunto = new File(archivoAdjuntoFilePath);

                                                    //commons-io
                                                    FileUtils.copyInputStreamToFile(in, fileArchivoAdjunto);
                                                }
                                            } else {
                                                throw new ExcepcionBusinessAdendaAutomatica("NoHayRutaAlfresco");
                                            }

                                            LOGGER.info("/Nomnbre del archivo recibido " + resultUploadFilePdf.get("nombreArchivoDisco"));
                                            LOGGER.info("/Nomnbre de la plantilla " + fileArchivoAdjunto.getAbsolutePath());

                                            byte[] docRecibido = docClient.descargarDocumentoAndMerge(new File(cydocConfig.getCarpetaArchivosSubidos() + File.separator + resultUploadFilePdf.get("nombreArchivoDisco")), fileArchivoAdjunto);

                                            LOGGER.info("/Numero del documento legal " + documentoLegal.getNumero());

                                            String convertedFileName = "] - " + documentoLegal.getNumero() + ".pdf";
                                            String convertedFilePath = cydocConfig.getCarpetaArchivosSubidos() + File.separator + convertedFileName;

                                            LOGGER.info("/Numero del convertedFilePath" + convertedFilePath);

                                            //Adjuntando borrador
                                            File fileBorrador = new File(convertedFilePath);
                                            FileUtils.writeByteArrayToFile(fileBorrador, docRecibido);
                                            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
                                            elaborarDocumentoService.guardarBorrador(documentoAdenda, convertedFileName, new Integer[]{hcDocumentoLegal.getResponsable().getId()}, usuario, false);

                                            List<RepresentanteComp> representantesCompanhia = usuarioRepresentanteCompaniaRepository.getList(Constantes.ESTADO_REPRESENTANTE_COMPANHIA_HABILITADO);
                                            Map<String, Object> resultFirmaElectronica = firmaElectronicaService.createContrato(idExpediente, usuario, Constantes.FIRMA_ELECTRONICA_LANG, representantesCompanhia.get(0).getRepresentante().getId(), Constantes.TIPO_FIRMA_ELECTRONICA_VIDEO_FIRMA, true);
                                            if (resultFirmaElectronica.get("resultado").equals("error")) {
                                                throw new ExcepcionBusinessAdendaAutomatica("FirmaElectronica");
                                            }

                                        } else {

                                            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
                                            elaborarDocumentoService.guardarBorrador(documentoAdenda, resultUploadFilePdf.get("nombreArchivoDisco"), new Integer[]{hcDocumentoLegal.getResponsable().getId()}, usuario, false);

                                            List<RepresentanteComp> representantesCompanhia = usuarioRepresentanteCompaniaRepository.getList(Constantes.ESTADO_REPRESENTANTE_COMPANHIA_HABILITADO);
                                            Map<String, Object> resultFirmaElectronica = firmaElectronicaService.createContrato(idExpediente, usuario, Constantes.FIRMA_ELECTRONICA_LANG, representantesCompanhia.get(0).getRepresentante().getId(), Constantes.TIPO_FIRMA_ELECTRONICA_VIDEO_FIRMA, true);
                                            if (resultFirmaElectronica.get("resultado").equals("error")) {
                                                throw new ExcepcionBusinessAdendaAutomatica("FirmaElectronica");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (ExcepcionAlfresco ex) {
                    LOGGER.error(ex.getMessage(), ex);
                    throw new RuntimeException("ALFRESCO");

                } catch (ExcepcionBusinessAdendaAutomatica ex) {
                    throw new RuntimeException(ex.getMessage());

                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new RuntimeException(e.getMessage());
                }
                return expediente.getId();
            }
        }
        return 0;
    }

    @Override
    public ResponseModelFile subirArchivo(MultipartFile archivoSubir, Integer idUsuario) {

        ResponseModelFile responseModel = new ResponseModelFile();
        Usuario usuario = usuarioRepository.findById(idUsuario);

        if (usuario != null) {

            if (archivoSubir.getSize() > 0) {
                
                int numeroGenerado = new Random().nextInt(999999999);
                String archivo = archivoSubir.getOriginalFilename();
                String nombreFormateado = AppUtil.quitarAcentos(archivo);
                nombreFormateado = org.apache.commons.lang3.StringUtils.stripAccents(nombreFormateado);
                List<Parametro> caracteresEspeciales = parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_CARACTERES_ESPECIALES);
                
                ArrayList<String> caracteresReemplazar = new ArrayList<>();
                for (Parametro parametro : caracteresEspeciales) {
                    caracteresReemplazar.add(parametro.getValor());
                }
                String fileNameWithoutExt = FilenameUtils.getBaseName(archivo);
                String extension = FilenameUtils.getExtension(archivo);
                String fileNameWithoutExtFormated = AppUtil.quitarCaracteres(fileNameWithoutExt, caracteresReemplazar);
                nombreFormateado = fileNameWithoutExtFormated + "." + extension;   
                
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
                    LOGGER.info("Archivo subido: " + fileName);
                    LOGGER.info("archivo" + nombreFormateado);
                    LOGGER.info("random" + numeroGenerado);
                    LOGGER.info("nombreArchivoDisco" + nombreSubir);

                    responseModel.setRandom(String.valueOf(numeroGenerado));
                    responseModel.setArchivo(nombreFormateado);
                    responseModel.setNombreArchivoDisco(nombreSubir);
                    responseModel.setMessage("Exito");
                    responseModel.setHttpSatus(HttpStatus.CREATED);

                    return responseModel;

                } catch (IOException e) {
                    responseModel.setMessage("Ocurri\u00f3 un error subiendo el archivo");
                    responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    LOGGER.info(e.getMessage(), e);
                }
            }
            responseModel.setMessage("Sin adjuntos");
            responseModel.setHttpSatus(HttpStatus.NOT_FOUND);

            return responseModel;
        }

        responseModel.setMessage("No se encontro usuario");
        responseModel.setHttpSatus(HttpStatus.CONFLICT);

        return responseModel;
    }

    @Override
    public Expediente obtenerExpedienteNuevo(Integer idExpedienteRegistrado) {

        Expediente expediente = expedienteRepository.findById(idExpedienteRegistrado);
        List<Documento> documentos = documentoRepository.obtenerPorExpediente(expediente.getId());

        for (Documento documento : documentos) {
            List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documento.getId());
            LOGGER.info("documento.getId(): " + documento.getId());
            LOGGER.info("archivos.size(): " + archivos.size());
            for (Archivo archivo : archivos) {
                archivo.setNuevo(true);
            }
            documento.setArchivos(archivos);
        }
        expediente.setDocumentos(documentos);

        return expediente;
    }

    @Override
    public Expediente obtenerExpediente(Integer idExpediente) {
        return expedienteRepository.obtenerExpedienteConDocumentos(idExpediente);
    }

    @Override
    public void subirArchivosAlfresco(Expediente expediente, Usuario usuario) throws ExcepcionAlfresco {
        LOGGER.info("subirArchivosAlfresco 44444");
        List<Documento> documentosExpediente = expediente.getDocumentos();
        alfrescoService.subirArchivos(documentosExpediente);
        adjuntarArchivoService.versionarArchivo(documentosExpediente, usuario);
    }

    /**
     * Metodo que registra la primera traza del expediente.
     */
    private Map<String, Object> guardarTrazaExpediente(Usuario usuario, Expediente expediente, Usuario responsable, String observacion) throws Exception {

        HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(expediente.getId());
        Traza trazaExistente = trazaRepository.obtenerPrimeraTrazaxExpediente(expediente.getId(), Constantes.ACCION_ENVIAR_SOLICITUD, Constantes.ACCION_REGISTRO_MANUAL);
        Map<String, Object> mapResultado = new HashMap<String, Object>();
        Integer idPerfil = Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_PERFIL_USUARIOFINAL).get(0).getValor());
        Perfil perfilSesion = perfilRepository.findById(idPerfil);

        if (trazaExistente != null) {
            LOGGER.info("Se intento registrar una doble traza para el expediente " + expediente.getId());
            mapResultado.put("traza", trazaExistente);
            mapResultado.put("trazaExistente", true);
        } else {
            List<Usuario> usuariosProcesos = null;
            if (responsable != null) {
                usuariosProcesos = new ArrayList<Usuario>();
                usuariosProcesos.add(responsable);
                //usuariosProcesos.add(usuario);
            } else {
                usuariosProcesos = usuarioRepository.obtenerResponsablesPorProceso(expediente.getProceso().getId());
            }
            LOGGER.info("usuariosProcesos :" + usuariosProcesos.size());

            expediente.setEstado(Constantes.ESTADO_REGISTRADO);
            expediente.setObservacionMp(observacion);

            LOGGER.info("------------------Estado del expediente----------------------");
            LOGGER.info(expediente.getEstado());
            LOGGER.info("----------------Estado del documento legal-------------------");
            LOGGER.info(documentoLegal.getEstado());
            LOGGER.info("-------------------------------------------------------------");
            
            
            //joel
            
			if (documentoLegal.getAdenda() != null) {
				// Documento legal con adendas
				if (documentoLegal.getAdenda().getHcTipoContrato() != null) {
					// Documento legal con adenda automatica
					HcTipoContratoConfiguracion hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegal.getAdenda().getHcTipoContrato().getId());

					if (!hcTipoContratoConfiguracion.getEstadoSolicitud()
							.equals(Constantes.ESTADO_HC_ENVIADO_FIRMA.toString())) {
						if (documentoLegal.getEstado() == Constantes.ESTADO_HC_VIGENTE) {
							LOGGER.info("ESTADO VIGENTE T");
							expediente.setEstado(Constantes.ESTADO_ARCHIVADO);
						}
					}

				}
			}
                        	
            

            expedienteRepository.save(expediente);

            Traza traza = new Traza();
            traza.setExpediente(expediente);
            traza.setRemitente(usuario);
            traza.setActual(true);
            traza.setFechaCreacion(new Date());
            traza.setAccion(Constantes.ACCION_ENVIAR_SOLICITUD);
            traza.setOrden(1);
            traza.setPrioridad(parametroRepository.findObtenerPorTipoValor(Constantes.PARAMETRO_PRIORIDAD_EXPEDIENTE, Constantes.VALOR_PRIORIDAD_NORMAL));

            if ((perfilSesion != null && perfilSesion.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES)) ||
                    (perfilSesion != null && perfilSesion.getCodigo().equals(Constantes.PERFIL_DIGITALIZACION) ||
                            (perfilSesion != null && perfilSesion.getCodigo().equals(Constantes.PERFIL_USUARIO_FINAL)))) {
                traza.setObservacion(observacion);
            } else {
                traza.setObservacion("Creación del Expediente");
            }

            Proceso proceso = expediente.getProceso();
            traza.setProceso(proceso);

            if (proceso.getTipoProceso() != null && proceso.getTipoProceso().getCodigo().equals(Constantes.TIPO_PROCESO_WORKFLOW)) {

                Workflow workflow = workflowRepository.obtenerUltimaVersionPorProceso(proceso.getId());

                if (workflow == null) {
                    throw new Exception("No existe workflow para el proceso " + proceso.getLabel());
                }

                PasoWorkflow paso = pasoWorkflowRepository.obtenerPrimerPaso(workflow.getId());
                traza.setPaso(paso);
                traza.setActividad(paso.getNombre());
            }

            if (documentoLegal.getAdenda() != null) {

                LOGGER.info("-----------------------------------------------");
                LOGGER.info("------------Traza de Adendas-------------------");
                LOGGER.info("-----------------------------------------------");

                if (documentoLegal.getAdenda().getHcTipoContrato() != null) {

                    if (documentoLegal.getEstado() == Constantes.ESTADO_HC_ENVIADO_FIRMA) {

                        LOGGER.info("ESTADO ENVIADO A FIRMA F");
                        traza.setAccion(Constantes.ACCION_ENVIADO_FIRMA);
                        traza.setObservacion(Constantes.ACCION_ENVIADO_FIRMA);
                        traza.setRemitente(documentoLegal.getResponsable());
                    }

                    if (documentoLegal.getEstado() == Constantes.ESTADO_HC_VIGENTE) {

                        LOGGER.info("ESTADO VIGENTE T");
                        traza.setAccion(Constantes.ACCION_ARCHIVAR);
                        traza.setObservacion(Constantes.ACCION_ADENDA_VIGENTE);
                        traza.setRemitente(documentoLegal.getResponsable());
                    }
                }
            }

            trazaRepository.save(traza);

            List<Usuario> listDestinatarios = new ArrayList<>();
            for (Usuario u : usuariosProcesos) {
                listDestinatarios.add(u);
                UsuarioPorTraza ut = new UsuarioPorTraza(u, traza);
                ut.setResponsable(true);
                ut.setAprobado(false);
                ut.setBloqueado(false);
                ut.setLeido(false);
                usuarioPorTrazaRepository.save(ut);
            }
            mapResultado.put("traza", traza);
            mapResultado.put("listDestinatarios", listDestinatarios);
            mapResultado.put("trazaExistente", false);
        }

        return mapResultado;
    }

    private Map<String, String> generarPlantillaPdf(HcDocumentoLegal documentoLegal, Integer idHcTipoContrato, Usuario usuario) throws IOException {

        String outputFilePathName = generarHcDocumentoFromPlantillaService.generarHcDocumentoFromPlantilla(documentoLegal, idHcTipoContrato);
        byte[] docRecibido = docClient.descargarDocumento(new File(outputFilePathName));

        String convertedFileName = documentoLegal.getNumero() + ".pdf";
        String convertedFilePath = cydocConfig.getCarpetaGenerados() + File.separator + convertedFileName;

        //Adjuntando borrador
        File fileBorrador = new File(convertedFilePath);
        FileUtils.writeByteArrayToFile(fileBorrador, docRecibido);

        Map<String, String> resultUploadFilePdf = elaborarDocumentoService.subirArchivo(fileBorrador, usuario);

        LOGGER.info("--------------Documento Legal Id - registrarExpedienteSolicitudHC-------------");
        LOGGER.info("outputFilePathName = " + outputFilePathName);
        LOGGER.info("outputConvertedFilePath = " + convertedFilePath);
        LOGGER.info("fileBorrador.length() = " + fileBorrador.length());
        LOGGER.info("resultUploadFilePdf = " + resultUploadFilePdf.get("resultado"));
        LOGGER.info("------------------------------------------------------------------------------");

        return resultUploadFilePdf;
    }

    @Override
    public List<Proceso> getProcesos() {
        return procesoRepository.obtenerProcesosActivos();
    }


    @Override
    public ResponseModel eliminarArchivo(String file) {
        ResponseModel responseModel = new ResponseModel();
        try {
            if (file.length() == file.lastIndexOf('-') + 2) {
                //Es un archivo de la tabla de requisitos
                //Quitarle al arch el numero de fila
                file = file.substring(0, file.lastIndexOf('-'));
            }

            File archivo = new File(cydocConfig.getCarpetaArchivosSubidos() + File.separator + file);
            if (archivo.exists()) {
                if (archivo.delete()) {
                    LOGGER.info("Se elimin\u00f3 el archivo " + archivo.getAbsolutePath());
                }
            }
            responseModel.setMessage("OK");
            responseModel.setHttpSatus(HttpStatus.OK);
        } catch (Exception ex){
            responseModel.setMessage("FAIL TO DELETE FILE");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseModel;
    }
}