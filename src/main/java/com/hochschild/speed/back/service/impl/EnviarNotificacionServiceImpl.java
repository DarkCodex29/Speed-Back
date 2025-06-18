package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.ApplicationConfig;
import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.ArchivoNotificarDao;
import com.hochschild.speed.back.dao.ElaborarDocumentoDao;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.NotificacionContraparte;
import com.hochschild.speed.back.model.domain.speed.utils.NotificacionExterna;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.AdjuntarArchivoService;
import com.hochschild.speed.back.service.AuthService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.GridService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.AssertUtils;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.ThreadEnvioMail;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("EnviarNotificacionService")
public class EnviarNotificacionServiceImpl implements EnviarNotificacionService {

    private static final Logger LOGGER = Logger.getLogger(EnviarNotificacionServiceImpl.class.getName());
    private final CydocConfig cydocConfig;
    private final NotificacionesConfig notificacionesConfig;

    private final ParametroRepository parametroRepository;

    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    private final HcGrupoRepository hcGrupoRepository;

    private final NotificacionRepository notificacionRepository;

    private final UsuarioRepository usuarioRepository;

    private final TrazaRepository trazaRepository;

    private final TipoNotificacionRepository tipoNotificacionRepository;
    
    private final DocumentoRepository documentoRepository;

    private final AuthService authService;

    private final GridService gridService;
    
    private final AdjuntarArchivoService adjuntarArchivoService;
    
    private final ArchivoRepository archivoRepository;

    private final ElaborarDocumentoDao elaborarDocumentoDao;
    
    private final ArchivoNotificarDao archivoNotificarDao;

    private final ApplicationConfig applicationConfig;
    
    @Autowired
    public EnviarNotificacionServiceImpl(
            CydocConfig cydocConfig
            , NotificacionesConfig notificacionesConfig
            , ParametroRepository parametroRepository
            , HcDocumentoLegalRepository hcDocumentoLegalRepository
            , HcGrupoRepository hcGrupoRepository
            , NotificacionRepository notificacionRepository
            , UsuarioRepository usuarioRepository
            , TrazaRepository trazaRepository
            , TipoNotificacionRepository tipoNotificacionRepository
            , DocumentoRepository documentoRepository
            , AuthService authService
            , GridService gridService
            , AdjuntarArchivoService adjuntarArchivoService
            , ArchivoRepository archivoRepository
            , ElaborarDocumentoDao elaborarDocumentoDao
            , ArchivoNotificarDao archivoNotificarDao
            , ApplicationConfig applicationConfig
    ) {
        this.cydocConfig = cydocConfig;
        this.notificacionesConfig = notificacionesConfig;
        this.parametroRepository = parametroRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcGrupoRepository = hcGrupoRepository;
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.trazaRepository = trazaRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.documentoRepository = documentoRepository;
        this.authService = authService;
        this.gridService = gridService;
        this.adjuntarArchivoService = adjuntarArchivoService;
        this.archivoRepository = archivoRepository;
        this.elaborarDocumentoDao = elaborarDocumentoDao;
        this.archivoNotificarDao= archivoNotificarDao;
        this.applicationConfig = applicationConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer registrarNotificacion(List<HcFirmaElectronicaRepresentante> usersDestinatarios, TipoNotificacion tipoNotificacion, Traza traza, Object[] arguments, List<Map<String, String>> imagenesInline) {

        if (usersDestinatarios != null && !usersDestinatarios.isEmpty()) {

            if (tipoNotificacion != null) {

                System.out.println("--------------EnviarNotificacionService - RegistrarNotificacion-------------");
                System.out.println("Nombre tipo notificacion /-/-/ = " + tipoNotificacion.getNombre());
                System.out.println("----------------------------------------------------------------------------");


                File archivo = null;
                BufferedReader br = null;
                String titulo = null;
                String contenido = "";
                try {
                    archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + tipoNotificacion.getNombre());
                    if (archivo.exists()) {

                        System.out.println("ENTROOOOO");

                        HcDocumentoLegal documentoLegal = null;
                        br = new BufferedReader(new FileReader(archivo));
                        String linea;
                        boolean primera = true;
                        while ((linea = br.readLine()) != null) {
                            if (primera) {
                                titulo = linea;
                                primera = false;
                            } else {
                                contenido += linea;// + "\n\r";
                            }
                        }
                        if (traza != null) {
                            String argumento = traza.getExpediente().getNumero();

                            if (traza.getExpediente().getDocumentoLegal() != null) {

                                documentoLegal = hcDocumentoLegalRepository.findById(traza.getExpediente().getDocumentoLegal().getId());
                                String tipoDocumentoLegal = "";

                                if (documentoLegal.getContrato() != null) {
                                    tipoDocumentoLegal = Constantes.TIPO_DOCUMENTO_CONTRATO;
                                } else if (documentoLegal.getAdenda() != null) {
                                    tipoDocumentoLegal = Constantes.TIPO_DOCUMENTO_ADENDA;
                                }

                                titulo = MessageFormat.format(titulo, arguments);
                                contenido = MessageFormat.format(contenido, arguments);
                            } else {

                                if (traza.getActividad() != null) {
                                    argumento += " - Actividad: " + traza.getActividad();
                                }
                                if (tipoNotificacion.getCodigo().equals(notificacionesConfig.getAlertaExpediente())) {
                                    Date fechaFin = gridService.calcularFechaFinconPlazo(traza.getExpediente(), traza.getExpediente().getProceso());
                                    titulo = MessageFormat.format(titulo, arguments);
                                    contenido = MessageFormat.format(contenido, arguments);
                                } else {
                                    titulo = MessageFormat.format(titulo, arguments);
                                    contenido = MessageFormat.format(contenido, arguments);
                                }
                            }
                        }

                        System.out.println("----------------Process RegistrarNotificacion -------------------------");
                        System.out.println("NOMBRE DE TIPO DE NOTIFICACION = " + tipoNotificacion.getNombre());
                        System.out.println("CONSTANTE A En Elaboración = " + Constantes.TIPO_NOTIFICACION_SOLICITUD_ACEPTADA);
                        System.out.println("CONSTANTE B Documento Elaborado = " + Constantes.TIPO_NOTIFICACION_ADJUNTAR_BORRADOR);
                        System.out.println("CONSTANTE D Visado Aprobado = " + Constantes.TIPO_NOTIFICACION_VISADO_APROBADO);
                        System.out.println("CONSTANTE E Enviado a Firma = " + Constantes.TIPO_NOTIFICACION_ENVIO_FIRMO);
                        System.out.println("--------------------------------------------------------------------------------------");

                        for (int j = 0; j < usersDestinatarios.size(); j++) {
                            String[] destinatarios = new String[1];
                            HcFirmaElectronicaRepresentante destinatario = usersDestinatarios.get(j);

                            System.out.println("------------------------------------------------------------------------------------- ");
                            System.out.println("EnviarNotificacionService /-/-/ Cnt destinatarios = " + usersDestinatarios.size());
                            System.out.println("EnviarNotificacionService /-/-/ Nombres = " + usersDestinatarios.get(j).getNombre());
                            System.out.println("EnviarNotificacionService /-/-/ Apellidos = " + usersDestinatarios.get(j).getApellidoPaterno() + " " + usersDestinatarios.get(j).getApellidoMaterno());
                            System.out.println("EnviarNotificacionService /-/-/ ID= " + usersDestinatarios.get(j).getId());
                            System.out.println("------------------------------------------------------------------------------------- ");

                            destinatarios[0] = usersDestinatarios.get(j).getCorreo();

                            System.out.println("CORREO " + j + " : " + usersDestinatarios.get(j).getCorreo());

                            String contenidoEnvio = "";
                            String tituloEnvio = "";
                            if (documentoLegal != null) {
                                if (contenido.contains("[URL]")) {
                                    String url = destinatario.getEnlace();
                                    contenidoEnvio = contenido.replace("[URL]", url);
                                } else {
                                    contenidoEnvio = contenido;
                                }

                                if (tipoNotificacion.getCodigo().equals(notificacionesConfig.getEnviarVisado()) && j > 0) {
                                    tituloEnvio = "COPIA: " + titulo;
                                } else {
                                    tituloEnvio = titulo;
                                }

                            } else {
                                contenidoEnvio = contenido;
                                tituloEnvio = titulo;
                            }
                            //log.debug("Asunto ["+Util.convertirAcentosUTF(StringEscapeUtils.unescapeHtml(tituloEnvio))+"]");
                            tituloEnvio = AppUtil.convertirAcentosUTF(tituloEnvio);
                            ThreadEnvioMail mail = new ThreadEnvioMail(destinatarios, tituloEnvio, contenidoEnvio, imagenesInline);
                            Thread hiloMail = new Thread(mail, "Envio_Mail");
                            hiloMail.start();
                        }

                        /*
                         * Mail mail=new Mail(); String destinos=""; for(String
                         * destinatario : destinatarios){
                         * mail.agregarDestinatario(destinatario);
                         * destinos+=" "+destinatario+","; }
                         * log.debug("Enviando notificacion por correo a "
                         * +destinos); mail.setAsunto(titulo);
                         * mail.setContenido(contenido);
                         * if(mail.enviarCorreo()){
                         * log.info("Notificaciones por correo enviadas a "
                         * +destinos); } else{
                         * log.warn("No se pudo enviar el correo"); }
                         */
                        return Constantes.OK;
                    }

                    System.out.println("EnviarNotificacionService /-/- / registrarNotificacion - Fuera validacion archivo");
                    // FIXME es necesario manejar mejor los errores
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } catch (FileNotFoundException e) {
                    LOGGER.error("No se pudo registrar la notificacion. No se encontro el archivo de Notificacion", e);
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } catch (Exception e) {
                    LOGGER.error("No se pudo registrar la notificaci\u00f3n.", e);
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } finally {
                    try {
                        if (null != br) {
                            br.close();
                        }
                    } catch (Exception e) {
                        LOGGER.error("No se pudo cerrar el flujo.", e);
                        return Constantes.ERROR_DESTINO_INCORRECTO;
                    }
                }
            }
            return Constantes.ERROR_NOTIFICACION_INEXISTENTE;
        }
        return Constantes.ERROR_DESTINO_INCORRECTO;
    }

    //Se actualizo el rollbackFor
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer registrarNotificacion(List<Usuario> usersDestinatarios, TipoNotificacion tipoNotificacion, Traza traza) {

        if (usersDestinatarios != null && !usersDestinatarios.isEmpty()) {

            System.out.println("--------------EnviarNotificacionService - RegistrarNotificacion-------------");
            System.out.println("Nombre tipo notificacion /-/-/ = " + tipoNotificacion.getNombre());
            System.out.println("----------------------------------------------------------------------------");

            if (tipoNotificacion != null) {
                File archivo = null;
                BufferedReader br = null;
                String titulo = null;
                String contenido = "";
                try {
                    archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + tipoNotificacion.getNombre());
                    if (archivo.exists()) {

                        Integer idPerfilUF = Integer.parseInt(parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_ID_PERFIL_USUARIOFINAL).get(0).getValor());
                        HcDocumentoLegal documentoLegal = null;
                        br = new BufferedReader(new FileReader(archivo));
                        String linea;
                        boolean primera = true;
                        while ((linea = br.readLine()) != null) {
                            if (primera) {
                                titulo = linea;
                                primera = false;
                            } else {
                                contenido += linea;// + "\n\r";
                            }
                        }
                        if (traza != null) {
                            String argumento = traza.getExpediente().getNumero();

                            if (traza.getExpediente().getDocumentoLegal() != null) {

                                documentoLegal = hcDocumentoLegalRepository.findById(traza.getExpediente().getDocumentoLegal().getId());
                                String tipoDocumentoLegal = "";

                                if (documentoLegal.getContrato() != null) {
                                    tipoDocumentoLegal = Constantes.TIPO_DOCUMENTO_CONTRATO;
                                } else if (documentoLegal.getAdenda() != null) {
                                    tipoDocumentoLegal = Constantes.TIPO_DOCUMENTO_ADENDA;
                                }

                                /**
                                 * Argumentos para correos de Documentos Legales
                                 * 0 - Nombre Solicitante (Usuario) 1 - Nombre
                                 * Abogado Responsable (Usuario) 2 - Número
                                 * DocumentoLegal 3 - Tipo DocumentoLegal 4 -
                                 * Sumilla DocumentoLegal 5 - Compañía
                                 * DocumentoLegal 6 - Contraparte DocumentoLegal
                                 * 7 - Nombre remitente Traza 8 - Observación
                                 * Traza 9 - Número paso de Visado 10 - Estado
                                 * del Documento Legal 11 - Area del Remitente
                                 *
                                 */
                                Object[] arguments = {documentoLegal.getSolicitante().getNombres() + " " + documentoLegal.getSolicitante().getApellidos(),
                                        documentoLegal.getResponsable().getNombres() + " " + documentoLegal.getResponsable().getApellidos(),
                                        StringUtils.trimToEmpty(documentoLegal.getNumero()),
                                        tipoDocumentoLegal,
                                        StringUtils.trimToEmpty(documentoLegal.getSumilla()),
                                        documentoLegal.getArea().getCompania().getNombre(),
                                        documentoLegal.getContraparte().getLabel(),
                                        traza.getRemitente().getNombres() + " " + traza.getRemitente().getApellidos(),
                                        StringUtils.trimToEmpty(traza.getObservacion()),
                                        traza.getPasoVisado(),
                                        AppUtil.getNombreEstadoDL(documentoLegal.getEstado()),
                                        getAreaRemitente(traza.getRemitente().getUsuario())
                                };

                                titulo = MessageFormat.format(titulo, arguments);
                                contenido = MessageFormat.format(contenido, arguments);
                            } else {

                                if (traza.getActividad() != null) {
                                    argumento += " - Actividad: " + traza.getActividad();
                                }
                                if (tipoNotificacion.getCodigo().equals(notificacionesConfig.getAlertaExpediente())) {
                                    Date fechaFin = gridService.calcularFechaFinconPlazo(traza.getExpediente(), traza.getExpediente().getProceso());
                                    Object[] arguments = {argumento, fechaFin};
                                    titulo = MessageFormat.format(titulo, arguments);
                                    contenido = MessageFormat.format(contenido, arguments);
                                } else {
                                    Object[] arguments = {argumento};
                                    titulo = MessageFormat.format(titulo, arguments);
                                    contenido = MessageFormat.format(contenido, arguments);
                                }
                            }
                        }

                        System.out.println("----------------Process RegistrarNotificacion -------------------------");
                        System.out.println("NOMBRE DE TIPO DE NOTIFICACION = " + tipoNotificacion.getNombre());
                        System.out.println("CONSTANTE A En Elaboración = " + Constantes.TIPO_NOTIFICACION_SOLICITUD_ACEPTADA);
                        System.out.println("CONSTANTE B Documento Elaborado = " + Constantes.TIPO_NOTIFICACION_ADJUNTAR_BORRADOR);
                        System.out.println("CONSTANTE D Visado Aprobado = " + Constantes.TIPO_NOTIFICACION_VISADO_APROBADO);
                        System.out.println("CONSTANTE E Enviado a Firma = " + Constantes.TIPO_NOTIFICACION_ENVIO_FIRMO);
                        System.out.println("--------------------------------------------------------------------------------------");

                        if (tipoNotificacion.getNombre().equals(Constantes.TIPO_NOTIFICACION_SOLICITUD_ACEPTADA)
                                || tipoNotificacion.getNombre().equals(Constantes.TIPO_NOTIFICACION_ADJUNTAR_BORRADOR)
                                || tipoNotificacion.getNombre().equals(Constantes.TIPO_NOTIFICACION_VISADO_APROBADO)
                                || tipoNotificacion.getNombre().equals(Constantes.TIPO_NOTIFICACION_ENVIO_FIRMO)) {

                            System.out.println("ID SOLICITANTE EN DOCUMENTO LEGAL = " + documentoLegal.getSolicitante().getId());

                            //Verificando si el usuario es un jefe
                            if (documentoLegal.getSolicitante().getJefe() != null) {

                                System.out.println("TIENE JEFE");
                                List<HcGrupo> grupoPertenece = hcGrupoRepository.obtenerGrupoPorUsuario(documentoLegal.getSolicitante().getId());

                                if (grupoPertenece != null && !grupoPertenece.isEmpty()) {

                                    Integer idGrupo;

                                    for (int i = 0; i < grupoPertenece.size(); i++) {

                                        if (grupoPertenece.get(i).getTipoGrupo().getDescripcion().equals(Constantes.TIPO_GRUPO_AVISO_SOLICITUD)) {

                                            System.out.println("ID DEL GRUPO = " + grupoPertenece.get(i).getId());
                                            System.out.println("NOMBRE DEL TIPO DE GRUPO QUE ES = " + grupoPertenece.get(i).getTipoGrupo().getDescripcion());
                                            System.out.println("NOMBRE DE TIPO DE NOTIFICACION = " + tipoNotificacion.getNombre());
                                            idGrupo = grupoPertenece.get(i).getId();

                                            List<Usuario> usuariosFinales = hcGrupoRepository.obtenerUsuariosGrupoSolicitud(idGrupo);
                                            System.out.println("CANTIDAD DE USUARIOS = " + usuariosFinales.size());

                                            for (int uf = 0; uf < usuariosFinales.size(); uf++) {

                                                Integer A = usuariosFinales.get(uf).getId();
                                                Integer B = documentoLegal.getSolicitante().getId();
                                                System.out.println("USUARIO A TRATAR : " + A);
                                                System.out.println("USUARIO B TRATAR : " + B);

                                                if (!usuariosFinales.get(uf).getId().equals(documentoLegal.getSolicitante().getId())) {
                                                    System.out.println("USUARIO AGREGADO AL LISTADO usersDestinatarios = " + usuariosFinales.get(uf).getId());
                                                    usersDestinatarios.add(usuariosFinales.get(uf));
                                                }
                                            }

                                            Usuario usuarioJefe = documentoLegal.getSolicitante().getJefe();
                                            System.out.println("USUARIO JEFE AGREGADO= " + usuarioJefe.getId());
                                            //Agregando jefe del subordinado
                                            usersDestinatarios.add(usuarioJefe);
                                        }
                                    }
                                }
                            } else if (documentoLegal.getSolicitante().getJefe() == null) {

                                System.out.println("NO TIENE JEFE");
                                List<HcGrupo> grupoPertenece = hcGrupoRepository.obtenerGrupoPorUsuario(documentoLegal.getSolicitante().getId());

                                if (grupoPertenece != null && !grupoPertenece.isEmpty()) {

                                    Integer idGrupo;

                                    for (int i = 0; i < grupoPertenece.size(); i++) {

                                        if (grupoPertenece.get(i).getTipoGrupo().getDescripcion().equals(Constantes.TIPO_GRUPO_AVISO_SOLICITUD)) {

                                            System.out.println("ID DEL GRUPO = " + grupoPertenece.get(i).getId());
                                            System.out.println("NOMBRE DEL TIPO DE GRUPO QUE ES = " + grupoPertenece.get(i).getTipoGrupo().getDescripcion());
                                            System.out.println("NOMBRE DE TIPO DE NOTIFICACION = " + tipoNotificacion.getNombre());
                                            idGrupo = grupoPertenece.get(i).getId();

                                            List<Usuario> usuariosFinales = hcGrupoRepository.obtenerUsuariosGrupoSolicitud(idGrupo);
                                            System.out.println("CANTIDAD DE USUARIOS = " + usuariosFinales.size());

                                            for (int uf = 0; uf < usuariosFinales.size(); uf++) {

                                                Integer A = usuariosFinales.get(uf).getId();
                                                Integer B = documentoLegal.getSolicitante().getId();
                                                System.out.println("USUARIO A TRATAR : " + A);
                                                System.out.println("USUARIO B TRATAR : " + B);

                                                if (!usuariosFinales.get(uf).getId().equals(documentoLegal.getSolicitante().getId())) {
                                                    System.out.println("USUARIO AGREGADO AL LISTADO usersDestinatarios = " + usuariosFinales.get(uf).getId());
                                                    usersDestinatarios.add(usuariosFinales.get(uf));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for (int j = 0; j < usersDestinatarios.size(); j++) {
                            String[] destinatarios = new String[1];
                            Usuario destinatario = usersDestinatarios.get(j);

                            System.out.println("------------------------------------------------------------------------------------- ");
                            System.out.println("EnviarNotificacionService /-/-/ Cnt destinatarios = " + usersDestinatarios.size());
                            System.out.println("EnviarNotificacionService /-/-/ Nombres = " + usersDestinatarios.get(j).getNombres());
                            System.out.println("EnviarNotificacionService /-/-/ Apellidos = " + usersDestinatarios.get(j).getApellidos());
                            System.out.println("EnviarNotificacionService /-/-/ Usuario = " + usersDestinatarios.get(j).getUsuario());
                            System.out.println("EnviarNotificacionService /-/-/ ID= " + usersDestinatarios.get(j).getId());
                            System.out.println("------------------------------------------------------------------------------------- ");

                            Notificacion nuevo = new Notificacion();
                            nuevo.setTitulo(titulo);
                            nuevo.setContenido(contenido);
                            nuevo.setFechaCreacion(new Date());
                            nuevo.setRemitente(destinatario);
                            nuevo.setTipoNotificacion(tipoNotificacion);
                            nuevo.setTraza(traza);
                            nuevo.setLeido(false);
                            notificacionRepository.save(nuevo);
                            destinatarios[0] = usersDestinatarios.get(j).getCorreo();

                            String contenidoEnvio = "";
                            String tituloEnvio = "";
                            if (documentoLegal != null) {
                            	if (contenido.contains("[URLFILE]")) {//JRF
									Integer idArchivo = archivoNotificarDao.obtenerIdArchivoNotificar(documentoLegal.getId());
									Archivo archivoEnviar = archivoRepository.findById(idArchivo);
									if (archivoEnviar != null) {
										String idAlfresco = adjuntarArchivoService
												.obtenerIdAlfresco(archivoEnviar.getId());
										String url = applicationConfig.getVerArchivoUrl();
										URIBuilder uriBuilder = new URIBuilder(url);
										uriBuilder.addParameter("codigoArchivo", idAlfresco);
										String finalUrl = uriBuilder.build().toString();
										contenidoEnvio = contenido.replace("[URLFILE]", finalUrl);
									} else {
										System.out
												.println("No se encontró el archivo para enviar a la notificación.");
									}    
                                } else if (contenido.contains("[URL]")) {
                                    String url = authService.generateExternalLink(destinatario.getUsuario(),traza.getId());
                                    contenidoEnvio = contenido.replace("[URL]", url);
                                } else {
                                    contenidoEnvio = contenido;
                                }

                                if (tipoNotificacion.getCodigo().equals(notificacionesConfig.getEnviarVisado()) && j > 0) {
                                    tituloEnvio = "COPIA: " + titulo;
                                } else {
                                    tituloEnvio = titulo;
                                }

                            } else {
                                contenidoEnvio = contenido;
                                tituloEnvio = titulo;
                            }
                            //log.debug("Asunto ["+Util.convertirAcentosUTF(StringEscapeUtils.unescapeHtml(tituloEnvio))+"]");
//                            for (int i = 0; i < destinatarios.length; i++) {
//                                System.out.println(destinatarios[i]);
//                            }
                            
                            tituloEnvio = AppUtil.convertirAcentosUTF(tituloEnvio);
                            ThreadEnvioMail mail = new ThreadEnvioMail(destinatarios, tituloEnvio, contenidoEnvio);
                            Thread hiloMail = new Thread(mail, "Envio_Mail");
                            hiloMail.start();
                        }

                        /*
                         * Mail mail=new Mail(); String destinos=""; for(String
                         * destinatario : destinatarios){
                         * mail.agregarDestinatario(destinatario);
                         * destinos+=" "+destinatario+","; }
                         * log.debug("Enviando notificacion por correo a "
                         * +destinos); mail.setAsunto(titulo);
                         * mail.setContenido(contenido);
                         * if(mail.enviarCorreo()){
                         * log.info("Notificaciones por correo enviadas a "
                         * +destinos); } else{
                         * log.warn("No se pudo enviar el correo"); }
                         */
                        return Constantes.OK;
                    }

                    System.out.println("EnviarNotificacionService /-/- / registrarNotificacion - Fuera validacion archivo");
                    // FIXME es necesario manejar mejor los errores
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } catch (FileNotFoundException e) {
                    LOGGER.error("No se pudo registrar la notificacion. No se encontro el archivo de Notificacion", e);
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } catch (Exception e) {
                    LOGGER.error("No se pudo registrar la notificaci\u00f3n.", e);
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } finally {
                    try {
                        if (null != br) {
                            br.close();
                        }
                    } catch (Exception e) {
                        LOGGER.error("No se pudo cerrar el flujo.", e);
                        return Constantes.ERROR_DESTINO_INCORRECTO;
                    }
                }
            }
            return Constantes.ERROR_NOTIFICACION_INEXISTENTE;
        }
        return Constantes.ERROR_DESTINO_INCORRECTO;
    }

    private String getAreaRemitente(String usuario) {
        return elaborarDocumentoDao.ubicacionUsuarioBean(usuario);
    }


    @Override
    @Transactional
    public Integer registrarNotificacion(List<String> usersDestinatarios, String codigoTipoNotificacion, Integer idExpediente) {
        Traza traza = trazaRepository.obtenerUltimaTrazaPorExpediente(idExpediente);
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(codigoTipoNotificacion);
        List<Usuario> usuarios = new ArrayList<>();
        for (String string : usersDestinatarios) {
            Usuario u = usuarioRepository.obtenerPorUsuario(string);
            if (u != null) {
                usuarios.add(u);
            }
        }
        return registrarNotificacion(usuarios, tipoNotificacion, traza);
    }

    @Override
    @Transactional
    public Integer registrarNotificacionCopia(Usuario usuario, TipoNotificacion tipoNotificacion, TrazaCopia trazaCopia) {
        File archivo = null;
        BufferedReader br = null;
        String titulo = null;
        String contenido = "";
        try {
            archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + tipoNotificacion.getNombre());
            if (archivo.exists()) {
                br = new BufferedReader(new FileReader(archivo));
                String linea;
                int i = 0;
                while ((linea = br.readLine()) != null) {
                    if (i == 0) {
                        titulo = linea;
                    } else {
                        contenido += linea + "\n\r";
                    }
                    i++;
                }

                String argumento = trazaCopia.getReferencia().getExpediente().getNumero();
                if (trazaCopia.getReferencia().getActividad() != null) {
                    argumento += " - Actividad: " + trazaCopia.getReferencia().getActividad();
                }
                Object[] arguments = {argumento};
                titulo = MessageFormat.format(titulo, arguments);
                contenido = MessageFormat.format(contenido, arguments);

                Notificacion nuevo = new Notificacion();
                nuevo.setTitulo(titulo);
                nuevo.setContenido(contenido);
                nuevo.setFechaCreacion(new Date());
                nuevo.setRemitente(usuario);
                nuevo.setTipoNotificacion(tipoNotificacion);
                nuevo.setTrazaCopia(trazaCopia);
                nuevo.setTraza(trazaCopia.getReferencia());
                nuevo.setLeido(false);
                notificacionRepository.save(nuevo);

                if (!StringUtils.isEmpty(usuario.getCorreo())) {
                    String[] correo = {usuario.getCorreo()};
                    ThreadEnvioMail mail = new ThreadEnvioMail(correo, titulo, contenido);
                    Thread hiloMail = new Thread(mail, "Envio_Mail");
                    hiloMail.start();
                }

                return Constantes.OK;
            }
            return Constantes.ERROR_DESTINO_INCORRECTO;
        } catch (FileNotFoundException e) {
            LOGGER.error("No se pudo registrar la notificacion. No se encontro el archivo de Notificacion", e);
            return Constantes.ERROR_DESTINO_INCORRECTO;
        } catch (Exception e) {
            LOGGER.error("No se pudo registrar la notificaci\u00f3n.", e);
            return Constantes.ERROR_DESTINO_INCORRECTO;
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (Exception e) {
                LOGGER.error("No se pudo cerrar el flujo.", e);
                return Constantes.ERROR_DESTINO_INCORRECTO;
            }
        }
    }

    @Override
    @Transactional
    public Integer registrarNotificacion(Usuario destinatario, String tipoNotificacion) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(destinatario);
        TipoNotificacion tn = tipoNotificacionRepository.obtenerPorCodigo(tipoNotificacion);
        if (tn == null) {
            LOGGER.warn("No se ha registrado la notificacion con codigo [" + tipoNotificacion + "]");
        }
        return registrarNotificacion(usuarios, tn, null);
    }

    @Override
    @Transactional
    public Integer registrarNotificacion(Usuario destinatario, TipoNotificacion tipoNotificacion) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(destinatario);
        return registrarNotificacion(usuarios, tipoNotificacion, null);
    }

    @Override
    @Transactional
    public Integer registrarNotificacion(Usuario destinatario, TipoNotificacion tipoNotificacion, Traza traza) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(destinatario);
        return registrarNotificacion(usuarios, tipoNotificacion, traza);
    }

    @Override
    public Integer registrarNotificacionAlarma(List<Usuario> usersDestinatarios, TipoNotificacion tipoNotificacion, HcDocumentoLegal documentoLegal, String mensajeAlarma) {
        if (usersDestinatarios != null && usersDestinatarios.size() != 0) {
            if (tipoNotificacion != null) {
                File archivo = null;
                BufferedReader br = null;
                String titulo = null;
                String contenido = "";
                try {
                    archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + tipoNotificacion.getNombre());
                    if (archivo.exists()) {
                        br = new BufferedReader(new FileReader(archivo));
                        String linea;
                        boolean primera = true;
                        while ((linea = br.readLine()) != null) {
                            if (primera) {
                                titulo = linea;
                                primera = false;
                            } else {
                                contenido += linea;// + "\n\r";
                            }
                        }
                        if (documentoLegal != null) {
                            documentoLegal = hcDocumentoLegalRepository.findById(documentoLegal.getId());
                            String tipoDocumentoLegal = "";

                            if (documentoLegal.getContrato() != null) {
                                tipoDocumentoLegal = Constantes.TIPO_DOCUMENTO_CONTRATO;
                            } else if (documentoLegal.getAdenda() != null) {
                                tipoDocumentoLegal = Constantes.TIPO_DOCUMENTO_ADENDA;
                            }

                            /**
                             * Argumentos para correos de Documentos Legales 0 -
                             * Nombre Solicitante (Usuario) 1 - Nombre Abogado
                             * Responsable (Usuario) 2 - Número DocumentoLegal 3
                             * - Tipo DocumentoLegal 4 - Sumilla DocumentoLegal
                             * 5 - Compañía DocumentoLegal 6 - Contraparte
                             * DocumentoLegal 7 - Mensaje de la alarma
                             *
                             */
                            Object[] arguments = {documentoLegal.getSolicitante().getNombres() + " " + documentoLegal.getSolicitante().getApellidos(),
                                    documentoLegal.getResponsable().getNombres() + " " + documentoLegal.getResponsable().getApellidos(),
                                    documentoLegal.getNumero(),
                                    tipoDocumentoLegal,
                                    documentoLegal.getSumilla(),
                                    documentoLegal.getArea().getCompania().getNombre(),
                                    documentoLegal.getContraparte().getRazonSocial(),
                                    mensajeAlarma
                            };
                            titulo = MessageFormat.format(titulo, arguments);
                            contenido = MessageFormat.format(contenido, arguments);
                        }
                        String[] destinatarios = new String[usersDestinatarios.size()];
                        for (int j = 0; j < usersDestinatarios.size(); j++) {

                            Usuario destinatario = usersDestinatarios.get(j);

                            Notificacion nuevo = new Notificacion();
                            nuevo.setTitulo(titulo);
                            nuevo.setContenido(contenido);
                            nuevo.setFechaCreacion(new Date());
                            nuevo.setRemitente(destinatario);
                            nuevo.setTipoNotificacion(tipoNotificacion);
                            nuevo.setLeido(false);
                            notificacionRepository.save(nuevo);
                            destinatarios[j] = usersDestinatarios.get(j).getCorreo();
                        }

                        ThreadEnvioMail mail = new ThreadEnvioMail(destinatarios, titulo, contenido);
                        Thread hiloMail = new Thread(mail, "Envio_Mail");
                        hiloMail.start();

                        return Constantes.OK;
                    }
                    LOGGER.error("No se pudo registrar la notificacion. No se encontro el archivo de Notificacion");
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } catch (FileNotFoundException e) {
                    LOGGER.error("No se pudo registrar la notificacion. No se encontro el archivo de Notificacion", e);
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } catch (Exception e) {
                    LOGGER.error("No se pudo registrar la notificaci\u00f3n.", e);
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                } finally {
                    try {
                        if (null != br) {
                            br.close();
                        }
                    } catch (Exception e) {
                        LOGGER.error("No se pudo cerrar el flujo.", e);
                        return Constantes.ERROR_DESTINO_INCORRECTO;
                    }
                }
            }
            return Constantes.ERROR_NOTIFICACION_INEXISTENTE;
        }
        return Constantes.ERROR_DESTINO_INCORRECTO;
    }

    @Override
    public Integer registrarNotificacionPregunta(List<Usuario> usersDestinatarios, Usuario usuario, String pregunta) {
        if (usersDestinatarios != null) {
            File archivo = null;
            BufferedReader br = null;
            String titulo = null;
            String contenido = "";
            try {
                archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + "registroPregunta");
                if (archivo.exists()) {
                    br = new BufferedReader(new FileReader(archivo));
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        contenido += linea;
                    }
                    Object[] arguments = {
                            usuario.getNombreCompleto(),
                            pregunta
                    };
                    titulo = "SPEED 3.0: Solicitud de pregunta - Asistente Virtual";
                    contenido = MessageFormat.format(contenido, arguments);
                    if (contenido.contains("[URL]")) {
                        Map<String, String> urlParts = AppUtil.getUrlParts();
                        AssertUtils.isNotEmptyString(urlParts.get(Constantes.URLPARTS_SCHEME), "url schema was not received");
                        AssertUtils.isNotEmptyString(urlParts.get(Constantes.URLPARTS_SERVERNAME), "url server name was not received");
                        AssertUtils.isNotEmptyString(urlParts.get(Constantes.URLPARTS_SERVERPORT), "url server port was not received");
                        AssertUtils.isNotEmptyString(urlParts.get(Constantes.URLPARTS_CONTEXTPATH), "url context path was not received");
                        String url = urlParts.get(Constantes.URLPARTS_SCHEME) + "://" + urlParts.get(Constantes.URLPARTS_SERVERNAME) + ":" + urlParts.get(Constantes.URLPARTS_SERVERPORT) + urlParts.get(Constantes.URLPARTS_CONTEXTPATH);
                        contenido = contenido.replace("[URL]", url);
                    }
                    String[] destinatarios = new String[usersDestinatarios.size()];
                    for (int j = 0; j < usersDestinatarios.size(); j++) {
                        destinatarios[j] = usersDestinatarios.get(j).getCorreo();
                    }

                    ThreadEnvioMail mail = new ThreadEnvioMail(destinatarios, titulo, contenido);
                    Thread hiloMail = new Thread(mail, "Envio_Mail");
                    hiloMail.start();

                    return Constantes.OK;
                }
            } catch (FileNotFoundException e) {
                return Constantes.ERROR_DESTINO_INCORRECTO;
            } catch (Exception e) {
                return Constantes.ERROR_DESTINO_INCORRECTO;
            } finally {
                try {
                    if (null != br) {
                        br.close();
                    }
                } catch (Exception e) {
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                }
            }
        }
        return Constantes.ERROR_DESTINO_INCORRECTO;

    }


    @Override
    public Integer enviarNotificacionExterna(NotificacionExterna notificacionExterna) {
        if (notificacionExterna.getDestinatarios() != null) {
            System.out.println("--------------EnviarNotificacionService - EnviarNotificacionExtenra-------------");
            System.out.println("Nombre tipo notificacion /-/-/ = NotificacionExterna");
            System.out.println("----------------------------------------------------------------------------");

            File archivo = null;
            BufferedReader br = null;
            String titulo = null;
            String contenido = "";

            try {
                archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + "HC_Notificacion_Externa");
                if (archivo.exists()) {
                    br = new BufferedReader(new FileReader(archivo));
                    String linea;
                    boolean primera = true;
                    while ((linea = br.readLine()) != null) {
                        if (primera) {
                            titulo = linea;
                            primera = false;
                        } else {
                            contenido += linea; // + "\n\r";
                        }
                    }
                    br.close();

                    Object[] arguments = {
                            notificacionExterna.getNumeroSolicitud(),
                            notificacionExterna.getCompania(),
                            notificacionExterna.getContraparte(),
                            notificacionExterna.getDescripcionContrato(),
                            notificacionExterna.getRazonSocialContraparte()
                    };

                    assert titulo != null;
                    String tituloEnvio = MessageFormat.format(titulo, arguments);
                    String contenidoEnvio = MessageFormat.format(contenido, arguments);
                    String[] destinatarios = notificacionExterna.getDestinatarios().toArray(new String[0]);
                    List<File> archivosAdjuntos = notificacionExterna.getArchivos();

                    // Enviar el correo electrónico
                    ThreadEnvioMail mail = new ThreadEnvioMail(destinatarios, tituloEnvio, contenidoEnvio, new ArrayList<>(), archivosAdjuntos);
                    Thread hiloMail = new Thread(mail, "Envio_Mail");
                    hiloMail.start();

                    return Constantes.OK;
                } else {
                    LOGGER.error("No se encontró el archivo de notificación: " + archivo.getAbsolutePath());
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                }
            } catch (IOException e) {
                LOGGER.error("No se pudo registrar la notificación. No se encontró el archivo de Notificación", e);
                return Constantes.ERROR_DESTINO_INCORRECTO;
            } catch (Exception e) {
                LOGGER.error("Error al enviar la notificación", e);
                return Constantes.ERROR_DESTINO_INCORRECTO;
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("Error al cerrar el BufferedReader", e);
                }
            }
        }
        return Constantes.ERROR_DESTINO_INCORRECTO;
    }
    
    @Override
    public Integer enviarNotificacionContraparte(NotificacionContraparte notificacionContraparte) {
        if (notificacionContraparte.getDestinatarios() != null) {
            System.out.println("--------------EnviarNotificacionService - EnviarNotificacionContraparte-------------");
            System.out.println("Nombre tipo notificacion /-/-/ = NotificacionContraparte");
            System.out.println("Nombre Contratista: " + notificacionContraparte.getNombreContratista());
            System.out.println("Numero Solicitud: " + notificacionContraparte.getNumeroSolicitud());
            System.out.println("Sumilla: " + notificacionContraparte.getSumilla());
            System.out.println("Correos Internos: " + (notificacionContraparte.getCorreosInternos() != null ? "Incluidos" : "No incluidos"));
            System.out.println("----------------------------------------------------------------------------");

            File archivo = null;
            BufferedReader br = null;
            String titulo = null;
            String contenido = "";

            try {
                archivo = new File(cydocConfig.getCarpetaNotificaciones() + File.separator + "HC_Borrador_Contrato");
                if (archivo.exists()) {
                    br = new BufferedReader(new FileReader(archivo));
                    String linea;
                    boolean primera = true;
                    while ((linea = br.readLine()) != null) {
                        if (primera) {
                            titulo = linea;
                            primera = false;
                        } else {
                            contenido += linea; // + "\n\r";
                        }
                    }
                    br.close();

                    Object[] arguments = {
                    		notificacionContraparte.getNombreContratista(),
                    		notificacionContraparte.getSumilla(),
                    		notificacionContraparte.getNumeroSolicitud(),
                    		notificacionContraparte.getCorreosInternos() != null ? notificacionContraparte.getCorreosInternos() : ""
                    };

                    assert titulo != null;
                    String tituloEnvio = MessageFormat.format(titulo, arguments);
                    String contenidoEnvio = MessageFormat.format(contenido, arguments);
                    String[] destinatarios = notificacionContraparte.getDestinatarios().toArray(new String[0]);
                    List<File> archivosAdjuntos = notificacionContraparte.getArchivos();

                    // Enviar el correo electrónico
                    ThreadEnvioMail mail = new ThreadEnvioMail(destinatarios, tituloEnvio, contenidoEnvio, new ArrayList<>(), archivosAdjuntos);
                    Thread hiloMail = new Thread(mail, "Envio_Mail");
                    hiloMail.start();

                    return Constantes.OK;
                } else {
                    LOGGER.error("No se encontró el archivo de notificación: " + archivo.getAbsolutePath());
                    return Constantes.ERROR_DESTINO_INCORRECTO;
                }
            } catch (IOException e) {
                LOGGER.error("No se pudo registrar la notificación. No se encontró el archivo de Notificación", e);
                return Constantes.ERROR_DESTINO_INCORRECTO;
            } catch (Exception e) {
                LOGGER.error("Error al enviar la notificación", e);
                return Constantes.ERROR_DESTINO_INCORRECTO;
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("Error al cerrar el BufferedReader", e);
                }
            }
        }
        return Constantes.ERROR_DESTINO_INCORRECTO;
    }

}
