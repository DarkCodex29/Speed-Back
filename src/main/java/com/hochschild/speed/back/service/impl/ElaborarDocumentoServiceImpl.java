package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.ElaborarDocumentoDao;
import com.hochschild.speed.back.dao.GrupoPersonalDao;
import com.hochschild.speed.back.model.bean.elaborarDocumento.DocumentoInfo;
import com.hochschild.speed.back.model.bean.elaborarDocumento.UsuarioDestinatarioBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.NotificacionContraparte;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service("ElaborarDocumentoService")
public class ElaborarDocumentoServiceImpl implements ElaborarDocumentoService {
    private static final Logger LOGGER = Logger.getLogger(ElaborarDocumentoServiceImpl.class.getName());
    private final CydocConfig cydocConfig;
    private final NotificacionesConfig notificacionesConfig;
    private final AdjuntarDocumentoService adjuntarDocumentoService;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final EnviarNotificacionService enviarNotificacionService;
    private final AdjuntarArchivoService adjuntarArchivoService;
    private final ExpedienteRepository expedienteRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final DocumentoRepository documentoRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcNumeracionRepository hcNumeracionRepository;

    private final FirmaElectronicaRepository firmaElectronicaRepository;

    private final FirmaElectronicaService firmaElectronicaService;

    private final ElaborarDocumentoDao elaborarDocumentoDao;

    private final GrupoPersonalDao grupoPersonalDao;

    private final TipoDocumentoRepository tipoDocumentoRepository;

    private final UsuarioPorTrazaRepository usuarioPorTrazaRepository;

    private final ParametroRepository parametroRepository;

    private final TrazaRepository trazaRepository;

    @Autowired
    public ElaborarDocumentoServiceImpl(CydocConfig cydocConfig,
                                        NotificacionesConfig notificacionesConfig,
                                        AdjuntarDocumentoService adjuntarDocumentoService,
                                        CommonBusinessLogicService commonBusinessLogicService,
                                        EnviarNotificacionService enviarNotificacionService,
                                        AdjuntarArchivoService adjuntarArchivoService,
                                        ExpedienteRepository expedienteRepository,
                                        TipoNotificacionRepository tipoNotificacionRepository,
                                        UsuarioRepository usuarioRepository,
                                        DocumentoRepository documentoRepository,
                                        HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                        HcNumeracionRepository hcNumeracionRepository,
                                        FirmaElectronicaRepository firmaElectronicaRepository,
                                        FirmaElectronicaService firmaElectronicaService,
                                        ElaborarDocumentoDao elaborarDocumentoDao,
                                        GrupoPersonalDao grupoPersonalDao,
                                        TipoDocumentoRepository tipoDocumentoRepository,
                                        UsuarioPorTrazaRepository usuarioPorTrazaRepository,
                                        ParametroRepository parametroRepository,
                                        TrazaRepository trazaRepository
    ) {
        this.cydocConfig = cydocConfig;
        this.notificacionesConfig = notificacionesConfig;
        this.adjuntarDocumentoService = adjuntarDocumentoService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.enviarNotificacionService = enviarNotificacionService;
        this.adjuntarArchivoService = adjuntarArchivoService;
        this.expedienteRepository = expedienteRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.documentoRepository = documentoRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcNumeracionRepository = hcNumeracionRepository;
        this.firmaElectronicaRepository = firmaElectronicaRepository;
        this.firmaElectronicaService = firmaElectronicaService;
        this.elaborarDocumentoDao = elaborarDocumentoDao;
        this.grupoPersonalDao = grupoPersonalDao;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.usuarioPorTrazaRepository = usuarioPorTrazaRepository;
        this.parametroRepository = parametroRepository;
        this.trazaRepository = trazaRepository;
    }

    @Override
    public ResponseModelFile subirArchivo(MultipartFile archivoSubir, Integer idUsuario, String rename) {

        ResponseModelFile responseModel = new ResponseModelFile();
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario != null) {
            if (archivoSubir.getSize() > 0) {
                int numeroGenerado = new Random().nextInt(999999999);
                String archivo = rename == "" ? archivoSubir.getOriginalFilename() : rename;
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
                    return responseModel;

                }
            }
            responseModel.setMessage("Sin adjuntos");
            responseModel.setHttpSatus(HttpStatus.NOT_FOUND);
            return responseModel;
        }
        responseModel.setMessage("Sin adjuntos");
        responseModel.setHttpSatus(HttpStatus.NOT_FOUND);
        return responseModel;
    }

    @Override
    public Map<String, String> subirArchivo(File archivoSubir, Usuario usuario) {

        Map<String, String> salida = new HashMap<>();
        String resultado = "error";

        if (archivoSubir.length() > 0) {
            int numeroGenerado = new Random().nextInt(999999999);
            String archivo = archivoSubir.getName();
            String nombreFormateado = AppUtil.quitarAcentos(archivo);
            String nombreSubir = "[" + numeroGenerado + "] - " + nombreFormateado;
            try {
                InputStream inputStream = new FileInputStream(archivoSubir);
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
                resultado = "exito";
                salida.put("archivo", archivo);
                salida.put("random", "" + numeroGenerado);
                salida.put("nombreArchivoDisco", nombreSubir);
            } catch (IOException e) {
                salida.put("msg", "Ocurri\u00f3 un error subiendo el archivo");
                LOGGER.info(e.getMessage(), e);
            }
        }
        salida.put("resultado", resultado);
        return salida;
    }

    @Override
    public Map<String, Object> guardarBorrador(Documento documento, String archivo, Integer[] idDestinatarios, Usuario usuario, Boolean enviadoC) {

        System.out.println("----------------ElaborarDocumentoService - GuardarBorrador-------------------------");
        System.out.println("------------------------------------------------------------------------------");

        final int archivosCant = trazaRepository.obtenerNumeroBorradores(documento.getExpediente().getId());

        Map<String, Object> result = new HashMap<>();
        Integer idArchivo = 0;
        if (archivo != null) {
            LOGGER.info("archivo :" + archivo);
            Archivo archivoSubir = null;
            documento = documentoRepository.findById(documento.getId());
            try {
                archivoSubir = adjuntarArchivoService.subirArchivo(documento, archivo, null, usuario, null);
                idArchivo = archivoSubir.getId();
            } catch (ExcepcionAlfresco e) {
                LOGGER.error("No se pudo subir el archivo.", e);
                result.put("resultado", "error");
                result.put("mensaje", "No se pudo subir el archivo.");
                return result;
            }
            LOGGER.debug("Archivo subir :" + archivoSubir.getRutaAlfresco());
            if (idArchivo > 0) {

                //para crear la carpeta final, cuando se sube el primer borrador.
                HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(documento.getExpediente().getId());
                if (documento.getTipoDocumento().getId().equals(Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_BORRADORES))
                        && archivosCant == 0 && documentoLegal.getResponsable().getUsuario() == usuario.getUsuario()) {
                    generarDocumento(documentoLegal.getExpediente(), usuario, documentoLegal.getNumero());
                }

                if (documentoLegal.getEstado().equals(Constantes.ESTADO_HC_ENVIADO_FIRMA)) {
                    HcFirmaElectronicaDocLegal hcFirmaElectronicaDocLegal = firmaElectronicaRepository.obtenerPorIdDocumentoLegal(documentoLegal.getId());
                    firmaElectronicaService.deleteFirmaElectronica(documento.getExpediente(), documentoLegal, hcFirmaElectronicaDocLegal, usuario, false, true);
                }

                if (documentoLegal.getAdenda() != null) {
                    //Trabajando con adendas.
                    if (documentoLegal.getAdenda().getHcTipoContrato() == null) {
                        System.out.println("RegistrarExpedienteSolicitudHC - Adenda");
                        documentoLegal.setEstado(Constantes.ESTADO_HC_ELABORADO);
                    }
                } else {
                    //Trabajando con contratos.
                    System.out.println("RegistrarExpedienteSolicitudHC - Contratos");
                    documentoLegal.setEstado(Constantes.ESTADO_HC_ELABORADO);
                }

                System.out.println("ESTADO DEL DOCUMENTO EN - Documento legal Elaborado/guardarBorrador");
                System.out.println(documentoLegal.getEstado());
                System.out.println("----------------------");
                hcDocumentoLegalRepository.save(documentoLegal);

                Map<String, Object> mapTrazaGenerada = commonBusinessLogicService.generarTraza(documento.getExpediente().getId(), usuario, documentoLegal.getResponsable(), Constantes.OBS_HC_DOCUMENTO_ELABORADO, Constantes.ACCION_ADJUNTAR_BORRADOR);

                //Notificar por correo a los destinatarios
                if (idDestinatarios != null && idDestinatarios.length > 0) {
                    List<Usuario> destinatarios = new ArrayList<Usuario>();
                    for (Integer idDestinatario : idDestinatarios) {
                        Usuario destinatario = usuarioRepository.findById(idDestinatario);
                        if (destinatario != null) {
                            destinatarios.add(destinatario);
                        }
                    }

                    adicionarUsuarioTraza((Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA), destinatarios);

                    String codigoNotificacion = notificacionesConfig.getAdjuntarBorrador();

                    //saber si se va mandar la notificacion de primer o segundo borrador.
                    if (documento.getTipoDocumento().getId().equals(Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_BORRADORES))) {

                        if (archivosCant == 0) {
                            codigoNotificacion = notificacionesConfig.getAdjuntarPrimerBorrador();
                        } else {
                            codigoNotificacion = notificacionesConfig.getAdjuntarSiguienteBorrador();
                        }
                    }

                    //Si esta marcdo el check, lo mando a contratista
                    if (enviadoC) {
                        documentoLegal.setFechaMovimiento(new Date());
                        documentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO, Constantes.PARAMETRO_SEGUIMIENTO_CONTRATISTA));
                        //joel
						List<File> files = new ArrayList<>();
						String rutaLocal = archivoSubir.getRutaLocal();
						File file = new File(rutaLocal);
						if (file.exists()) {
							files.add(file);
						} else {
							LOGGER.info("El archivo en la ruta " + rutaLocal + " no existe.");
						}

						// Obtener lista de correos internos desde procedimiento almacenado
						LOGGER.info("Obteniendo correos del grupo de personal usando SPEED_ADM_ListarGrupoPersonal");
						List<String> correosPersonal;
						try {
							correosPersonal = grupoPersonalDao.obtenerCorreosGrupoPersonal();
						} catch (Exception e) {
							LOGGER.error("Error al obtener correos del procedimiento almacenado", e);
							// Si falla el SP, continuar sin incluir correos internos
							correosPersonal = new ArrayList<>();
							LOGGER.warn("Continuando envio sin lista de correos internos debido a error en SP");
						}
						
						StringBuilder correosInternosBuilder = new StringBuilder();
						correosInternosBuilder.append("Enviar sus comentarios o revisión a los correos:\n");
						
						if (correosPersonal != null && !correosPersonal.isEmpty()) {
							for (String correo : correosPersonal) {
								correosInternosBuilder.append("- ").append(correo).append("\n");
							}
							// Remover el último salto de línea
							correosInternosBuilder.setLength(correosInternosBuilder.length() - 1);
							LOGGER.info("Se obtuvieron " + correosPersonal.size() + " correos del procedimiento almacenado");
						} else {
							LOGGER.warn("No se obtuvieron correos del procedimiento, usando mensaje generico");
							correosInternosBuilder.append("No hay correos disponibles en este momento");
						}

						NotificacionContraparte notificacionContraparte = new NotificacionContraparte();
						notificacionContraparte.setNombreContratista(documentoLegal.getContraparte().getRazonSocial());
						notificacionContraparte.setSumilla(documentoLegal.getSumilla());
						notificacionContraparte.setNumeroSolicitud(documentoLegal.getNumero());
						notificacionContraparte.setArchivos(files);
						notificacionContraparte.setCorreosInternos(correosInternosBuilder.toString());

						List<String> destinatariosContraparte = new ArrayList<>();
						if(documentoLegal.getCnt_correo_contacto()==null) {
							result.put("resultado", "error");
					        result.put("mensaje", "No se encontro el correo de la contraparte");
					        return result;
						}
						
						destinatariosContraparte.add(documentoLegal.getCnt_correo_contacto());
						notificacionContraparte.setDestinatarios(destinatariosContraparte);
						enviarNotificacionService.enviarNotificacionContraparte(notificacionContraparte);
						LOGGER.info("Se envio correo a la contraparte " + documentoLegal.getContraparte().getNombre()
								+ " (" + documentoLegal.getCnt_correo_contacto() + ") con lista de correos internos incluida.");
						hcDocumentoLegalRepository.save(documentoLegal);
                        
                    } else {
//                        if (documento.getTipoDocumento().getNombre().equals(Constantes.TIPO_DOCUMENTO_VERSION_FINAL)) {
//                            documentoLegal.setFechaMovimiento(new Date());
//                            documentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO, Constantes.PARAMETRO_SEGUIMIENTO_ADMINISTRACION_DE_CONTRATOS));
//
//                            hcDocumentoLegalRepository.save(documentoLegal);
//                        }
                        //si el primero se subbe a legal.
                        if (documento.getTipoDocumento().getNombre().equals(Constantes.TIPO_DOCUMENTO_BORRADORES) && archivosCant == 0) {

                            documentoLegal.setFechaMovimiento(new Date());
                            documentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO, Constantes.PARAMETRO_SEGUIMIENTO_LEGAL));

                            hcDocumentoLegalRepository.save(documentoLegal);
                        } else if (documento.getTipoDocumento().getNombre().equals(Constantes.TIPO_DOCUMENTO_BORRADORES) && destinatarios.size() > 0) {
                            // volver a vificar si es tipo borrador. modifica ubbicacion.

                            String ubicacion = elaborarDocumentoDao.ubicacionUsuarioBean(destinatarios.get(0).getUsuario());

                            if (ubicacion != null && documentoLegal.getUbicacionDocumento().getValor() != ubicacion) {
                                documentoLegal.setFechaMovimiento(new Date());
                                documentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO, ubicacion));

                                hcDocumentoLegalRepository.save(documentoLegal);
                            }
                        }
                    }

                    TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(codigoNotificacion);

                    if (documentoLegal.getAdenda() != null && documentoLegal.getAdenda().getHcTipoContrato() != null) {
                        System.out.println("ES ADENDA AUTOMATICA");
                        System.out.println("TipoNotificacion = " + tipoNotificacion.getNombre());
                    } else {
                        System.out.println("NO ES ADENDA AUTOMATICA");
                        System.out.println("TipoNotificacion = " + tipoNotificacion.getNombre());
                        enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA));
                    }
                }

                result.put("resultado", "exito");
                result.put("idDocumento", documento.getId());
                result.put("idExpediente", documento.getExpediente().getId());
                result.put("mensaje", "Archivo adjunto");
                return result;
            }
        }

        result.put("resultado", "error");
        result.put("mensaje", "Ocurrio un error al adjuntar el borrador");
        return result;
    }

    private void adicionarUsuarioTraza(Traza traza, List<Usuario> usersDestinatarios) {
        for (Usuario usuario : usersDestinatarios) {
            UsuarioPorTraza usuarioPorTraza = usuarioPorTrazaRepository.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
            if (usuarioPorTraza == null) {
                UsuarioPorTraza ut = new UsuarioPorTraza(usuario, traza);
                ut.setResponsable(false);
                ut.setAprobado(false);
                ut.setBloqueado(false);
                ut.setLeido(false);
                usuarioPorTrazaRepository.save(ut);
            }
        }
    }

    private void generarDocumento(Expediente expediente, Usuario userLogged, String numero) {

        TipoDocumento tipoDocumento = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_VERSION_FINAL);

        Documento documento = new Documento();
        documento.setExpediente(expediente);
        documento.setTipoDocumento(tipoDocumento);
        documento.setAutor(userLogged);
        documento.setTitulo(Constantes.TIPO_DOCUMENTO_VERSION_FINAL + " " + numero);
        documento.setFechaCreacion(new Date());
        documento.setFechaDocumento(new Date());
        documento.setNumero(numero);

        documentoRepository.save(documento);
    }

    @Override
    public List<HcDocumentoLegal> buscarDocumentosLegales(Map<String, Object> filtros) {
        return null;
    }

    @Override
    public List<HcDocumentoLegal> buscarDocumentosLegalesPorAbogadoResponsable(Integer idAbogadoResponsable) {
        return null;
    }

    @Override
    public List<HcDocumentoLegal> buscarAlarmasDeContratos(Integer idAbogadoResponsable, Integer idCompania, String numeroContrato, Date fechaInicio, Date fechaFin) {
        return null;
    }

    @Override
    public List<HcDocumentoLegal> buscarSeguimientoProcesos(Integer[] idSolicitante, Integer idCompania, Character estado, String idUbicacion) {
        return null;
    }

    @Override
    public List<Usuario> buscarSolicitanteDocumentoLegal() {
        return null;
    }

    @Override
    public List<HcTipoUbicacion> listaTiposUbicacion() {
        return null;
    }

    @Override
    public List<HcUbicacion> listarUbicacionPorTipo(Integer idtipo) {
        return null;
    }

    @Override
    public List<Integer> listaAnios() {

        return hcNumeracionRepository.getNumeracionAnio();
    }

    @Override
    public List<HcDocumentoLegal> buscarSeguimientoProcesosAll(Integer[] idSolicitanteSP, Integer idCompaniaSP, String[] idEstadoSP, String idUbicacionSP) {
        return null;
    }

    @Override
    public DocumentoInfo buscarDestinatariosDefecto(Integer id) {
        DocumentoInfo documentoInfo = new DocumentoInfo();

        Documento documento = commonBusinessLogicService.obtenerDocumento(id);
        HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegal(documento.getExpediente().getDocumentoLegal().getId());
        List<UsuarioDestinatarioBean> destinatariosDefecto = new ArrayList<>();

        destinatariosDefecto.add(new UsuarioDestinatarioBean(documentoLegal.getResponsable(), elaborarDocumentoDao.ubicacionUsuarioBean(documentoLegal.getResponsable().getUsuario()), "Responsable"));

        if (!documentoLegal.getSolicitante().getId().equals(documentoLegal.getResponsable().getId())) {
            destinatariosDefecto.add(new UsuarioDestinatarioBean(documentoLegal.getSolicitante(), elaborarDocumentoDao.ubicacionUsuarioBean(documentoLegal.getSolicitante().getUsuario()), "Solicitante"));
        }
        documentoInfo.setIdDocumento(documento.getId());
        documentoInfo.setDestinatariosDefecto(destinatariosDefecto);

        return documentoInfo;
    }

    @Override
    public List<UsuarioDestinatarioBean> buscarUsuariosUbicacion(List<Usuario> usuarios) {
        List<UsuarioDestinatarioBean> usuariosUbicacion = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            usuariosUbicacion.add(new UsuarioDestinatarioBean(usuario, elaborarDocumentoDao.ubicacionUsuarioBean(usuario.getUsuario()), ""));
        }

        return usuariosUbicacion;
    }
}
