package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.BotonDAO;
import com.hochschild.speed.back.dao.DocumentoDao;
import com.hochschild.speed.back.model.bean.RevisarDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.EstadoDocumentoDTO;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.ParametroService;
import com.hochschild.speed.back.service.RevisarDocumentoService;
import com.hochschild.speed.back.service.RevisarExpedienteService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service("RevisarDocumentoService")
public class RevisarDocumentoServiceImpl implements RevisarDocumentoService {
    private static final Logger LOGGER = Logger.getLogger(RevisarDocumentoServiceImpl.class.getName());

    private final DocumentoRepository documentoRepository;
    private final CampoPorDocumentoRepository campoPorDocumentoRepository;
    private final ArchivoRepository archivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TrazaRepository trazaRepository;
    private final RolRepository rolRepository;
    private final ParametroService parametroService;
    private final CydocConfig cydocConfig;
    private final VersionRepository versionRepository;
    private final RevisarExpedienteService revisarExpedienteService;
    private final ReemplazoRepository reemplazoRepository;
    private final HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository;
    private final NumeracionRepository numeracionRepository;
    private final DocumentoDao documentoDao;
private final BotonDAO botonDAO;
    private final PerfilRepository perfilRepository;
    @Autowired
    public RevisarDocumentoServiceImpl(
            DocumentoRepository documentoRepository,
            CampoPorDocumentoRepository campoPorDocumentoRepository,
            ArchivoRepository archivoRepository,
            UsuarioRepository usuarioRepository,
            TrazaRepository trazaRepository,
            RolRepository rolRepository,
            ParametroService parametroService,
            CydocConfig cydocConfig,
            VersionRepository versionRepository, RevisarExpedienteService revisarExpedienteService,
            ReemplazoRepository reemplazoRepository,
            HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository,
            NumeracionRepository numeracionRepository,
            DocumentoDao documentoDao,
            BotonDAO botonDAO, PerfilRepository perfilRepository) {
        this.documentoRepository = documentoRepository;
        this.campoPorDocumentoRepository = campoPorDocumentoRepository;
        this.archivoRepository = archivoRepository;
        this.usuarioRepository = usuarioRepository;
        this.trazaRepository = trazaRepository;
        this.rolRepository = rolRepository;
        this.parametroService = parametroService;
        this.cydocConfig = cydocConfig;
        this.versionRepository = versionRepository;
        this.revisarExpedienteService = revisarExpedienteService;
        this.reemplazoRepository = reemplazoRepository;
        this.hcTipoContratoConfiguracionRepository = hcTipoContratoConfiguracionRepository;
        this.numeracionRepository = numeracionRepository;
        this.documentoDao = documentoDao;
        this.botonDAO = botonDAO;
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Archivo obtenerArchivo(Integer idArchivo) {
        return archivoRepository.findById(idArchivo);
    }

    @Override
    public List<EstadoDocumentoDTO> listarEstadosDocumento(String codigoDocumento) {
        return documentoDao.listarEstadosDocumento(codigoDocumento);
    }

    @Override
    public RevisarDocumentoBean obtenerDocumento(Integer idUsuario, Integer idDocumento, Boolean soloLectura, Integer idPerfil) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        Perfil perfilSesion = perfilRepository.findById(idPerfil);
        LOGGER.info("SESSION ID: "+perfilSesion.getId() + " COD: "+ perfilSesion.getCodigo());
        Documento documento = documentoRepository.findById(idDocumento);
        List<CampoPorDocumento> campos = campoPorDocumentoRepository.getCamposPorDocumento(idDocumento);
        List<Archivo> archivos = obtenerArchivosxDocumento(documento);
        for(Archivo archivo : archivos){
            archivo.setDocumento(null);
            for(Version version : archivo.getVersions()){
                version.setArchivo(null);
            }

        }
        RevisarDocumentoBean revisarDocumentoBean = new RevisarDocumentoBean();
        revisarDocumentoBean.setCampos(campos);
        // Verificar si debe mostrar solo el último archivo
        boolean mostrarSoloUltimoArchivo = false;
        
        // Caso 1: Estado ENVIADO_VISADO para contratos, adendas y borradores
        if (documento.getExpediente().getDocumentoLegal().getEstado().equals(Constantes.ESTADO_HC_ENVIADO_VISADO) &&
                (documento.getTipoDocumento().getId().equals(Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_CONTRATO)) ||
                        documento.getTipoDocumento().getId().equals(Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_ADENDA)) ||
                        documento.getTipoDocumento().getId().equals(Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_BORRADORES)))) {
            mostrarSoloUltimoArchivo = true;
        }
        
        // Caso 2: Para documentos tipo BORRADORES, aplicar filtro por roles
        if (documento.getTipoDocumento().getId().equals(Integer.valueOf(Constantes.ID_TIPO_DOCUMENTO_BORRADORES))) {
            // Verificar roles del usuario
            List<Rol> roles = rolRepository.buscarActivosPorUsuario(usuario.getId());
            boolean tieneRolPrivilegiado = false;
            
            if (roles != null && !roles.isEmpty()) {
                for (Rol rol : roles) {
                    if (rol.getCodigo() != null && 
                        (rol.getCodigo().contains(Constantes.CODIGO_ROL_ABOGADO) ||
                         rol.getCodigo().equals(Constantes.ROL_ADMINISTRADOR_CODIGO))) {
                        tieneRolPrivilegiado = true;
                        break;
                    }
                }
            }
            
            // Si NO tiene rol privilegiado, mostrar solo último archivo
            if (!tieneRolPrivilegiado) {
                mostrarSoloUltimoArchivo = true;
            }
        }
        
        if (mostrarSoloUltimoArchivo && !archivos.isEmpty()) {
            List<Archivo> archivoReciente = new ArrayList<>();
            archivoReciente.add(archivos.get(0));
            for(Archivo archivo : archivoReciente){
                archivo.setDocumento(null);
                for(Version version : archivo.getVersions()){
                    version.setArchivo(null);
                }
            }
            revisarDocumentoBean.setLstArchivos(archivoReciente);
        } else {
            revisarDocumentoBean.setLstArchivos(archivos);
        }

        if (soloLectura == null || !soloLectura) {
            Traza traza = trazaRepository.obtenerUltimaTrazaPorExpediente(documento.getExpediente().getId());
            UsuarioPorTraza uxt = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), usuario.getId());
            if (uxt == null) {
                // Verificamos si el usuario esta reemplazando a otro usuario
                Reemplazo reemplazo = reemplazoRepository.buscarReemplazo(usuario.getId(), documento.getExpediente().getProceso(), null);
                if (reemplazo != null) {
                    uxt = revisarExpedienteService.obtenerUsuarioPorTraza(traza.getId(), reemplazo.getId());
                }
            }
            boolean responsable = uxt != null;

            //Listados nuevos
            List<Boton> ltsBotonesAutomatica = null;
            List<Boton> ltsBotonesAutomaticaContractual = null;

            List<Boton> ltsBotonesDoc = new ArrayList<>();

            if (!responsable) {
                List<Rol> roles = rolRepository.buscarActivosPorUsuario(usuario.getId());
                boolean rolAbogado = false;
                if (roles != null && !roles.isEmpty()) {
                    for (Rol rol : roles) {
                        if (rol.getCodigo().contains(Constantes.CODIGO_ROL_ABOGADO)) {
                            rolAbogado = true;
                        }
                    }
                }
                if (rolAbogado) {
                    responsable = true;
                }
            }

            LOGGER.info("--------------- validacion para botones INICIO -------------");
            LOGGER.info("---------------" + documento.getTipoDocumento().getId().toString() + " -------------" + parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_ID_CONTRATO).get(0).getValor());
            LOGGER.info("---------------" + documento.getTipoDocumento().getId().toString() + " -------------" + parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_ID_ADENDA).get(0).getValor());

            if (documento.getExpediente().getDocumentoLegal().getContrato()!= null){
                //Botones de contrato
                LOGGER.info("--------------- entra en primera validacion con Parametro ID CONTRATO-------------");
                String tipoDocumento = documento.getTipoDocumento().getNombre().toString().toLowerCase(); // Convertir a minúsculas para evitar problemas de mayúsculas/minúsculas
                if (tipoDocumento.contains("poderes") || tipoDocumento.contains("solicitud")) {
                    ltsBotonesDoc = obtenerBotonesDocumentoLegalExceptoParametroDocumento(perfilSesion, documento.getExpediente().getDocumentoLegal().getEstado(), responsable, Constantes.PARAMETRO_CONTRATO);

                }else{
                    ltsBotonesDoc = obtenerBotonesDocumentoLegalExceptoParametro(perfilSesion, documento.getExpediente().getDocumentoLegal().getEstado(), responsable, Constantes.PARAMETRO_ADENDA);

                }


                List<Boton> ltsBotones = new ArrayList<Boton>();
                for (Boton b : ltsBotonesDoc) {
                    if (b.getUrl().equals("enumerar")) {
                        if (usuario.getArea() != null) {
                            Numeracion numeracion = numeracionRepository.numeracionPorAreayTipoDocumento(usuario.getArea().getId(), documento.getTipoDocumento().getId());
                            if (numeracion != null & documento.getNumero() != null) {
                                if (documento.getNumero().equals(Constantes.NUMERACION_AUTOMATICA_NO_GENERADA)) {
                                    ltsBotones.add(b);
                                }
                            }
                        }
                    } else {
                        ltsBotones.add(b);
                    }
                }
                // Aplicar validación para botón "Generar Plantilla" 
                ltsBotones = aplicarValidacionGenerarPlantilla(ltsBotones, usuario);
                revisarDocumentoBean.setLtsBotonesDoc(ltsBotones);
               // revisarDocumentoBean.setLtsBotonesDoc(ltsBotonesDoc);

            } else
                if (documento.getTipoDocumento().getId().toString().equals(parametroService.buscarParametroPorTipo(Constantes.PARAMETRO_ID_ADENDA).get(0).getValor())
                    || documento.getTipoDocumento().getId().toString().equals(Constantes.PARAMETRO_ID_BORRADOR) // Borradores
                    || documento.getTipoDocumento().getId().toString().equals(Constantes.PARAMETRO_ID_VERSIONFINAL)//  Version Final
                ) {

                //Botones de adenda automatica
                if (documento.getExpediente().getDocumentoLegal().getAdenda().getHcTipoContrato() != null) {

                    HcTipoContratoConfiguracion hcTipoContratoConfiguracion = new HcTipoContratoConfiguracion();
                    hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documento.getExpediente().getDocumentoLegal().getAdenda().getHcTipoContrato().getId());

                    if (hcTipoContratoConfiguracion.getEsPlantilla().equals(Constantes.ESTADO_PLANTILLA_HABILITADA.toString())) {

                        System.out.println("---------------------------------");
                        System.out.println("Botones de unaa adenda con plantilla");
                        System.out.println(hcTipoContratoConfiguracion.getIdRecurso());
                        System.out.println("---------------------------------");

                        //Botones de una adenda con plantilla
                        ltsBotonesAutomaticaContractual = obtenerBotonAdendaAutomaticaPosicionContractual(perfilSesion, Constantes.RECURSO_PARA_ESTADO, responsable, Constantes.PARAMETRO_CONTRATO, hcTipoContratoConfiguracion.getIdRecurso());
                        ltsBotonesAutomatica = obtenerBotonesDocumentoLegalExceptoParametro(perfilSesion, documento.getExpediente().getDocumentoLegal().getEstado(), responsable, Constantes.PARAMETRO_CONTRATO);


                        System.out.println("Cantidad ltsBotonesAutomaticaContractual");
                        System.out.println(ltsBotonesAutomaticaContractual.size());

                        System.out.println("Cantidad ltsBotonesAutomatica");
                        System.out.println(ltsBotonesAutomatica.size());

                        ltsBotonesDoc.addAll(ltsBotonesAutomaticaContractual);
                        ltsBotonesDoc.addAll(ltsBotonesAutomatica);

                        System.out.println("Cantidad final");
                        System.out.println(ltsBotonesDoc.size());
                        System.out.println("---------------");

                    } else {

                        System.out.println("---------------------------------");
                        System.out.println("Botones de una adenda automtica");
                        System.out.println(hcTipoContratoConfiguracion.getEsPlantilla());
                        System.out.println("---------------------------------");
                        //Botones de una adenda automtica
                        ltsBotonesDoc = obtenerBotonesDocumentoLegalExceptoParametro(perfilSesion, documento.getExpediente().getDocumentoLegal().getEstado(), responsable, Constantes.PARAMETRO_CONTRATO);
                    }
                } else {

                    System.out.println("---------------------------------");
                    System.out.println("Botones de adenda normal");
                    System.out.println("---------------------------------");
                    //Botones de adenda normal
                    ltsBotonesDoc = obtenerBotonesDocumentoLegalExceptoParametro(perfilSesion, documento.getExpediente().getDocumentoLegal().getEstado(), responsable, Constantes.PARAMETRO_CONTRATO);
                }

                // Aplicar validación para botón "Generar Plantilla"
                ltsBotonesDoc = aplicarValidacionGenerarPlantilla(ltsBotonesDoc, usuario);
                revisarDocumentoBean.setLtsBotonesDoc(ltsBotonesDoc);
            } else {
                if (traza.getEstado() != null && traza.getEstado().equals(Constantes.ESTADO_PAUSADO)) {
                    ltsBotonesDoc = obtenerBotones(perfilSesion, documento.getExpediente().getEstado(), false);
                } else {
                    ltsBotonesDoc = obtenerBotones(perfilSesion, documento.getExpediente().getEstado(), responsable);
                }

                List<Boton> ltsBotones = new ArrayList<Boton>();
                for (Boton b : ltsBotonesDoc) {
                    if (b.getUrl().equals("enumerar")) {
                        if (usuario.getArea() != null) {
                            Numeracion numeracion = numeracionRepository.numeracionPorAreayTipoDocumento(usuario.getArea().getId(), documento.getTipoDocumento().getId());
                            if (numeracion != null & documento.getNumero() != null) {
                                if (documento.getNumero().equals(Constantes.NUMERACION_AUTOMATICA_NO_GENERADA)) {
                                    ltsBotones.add(b);
                                }
                            }
                        }
                    } else {
                        ltsBotones.add(b);
                    }
                }
                // Aplicar validación para botón "Generar Plantilla"
                ltsBotones = aplicarValidacionGenerarPlantilla(ltsBotones, usuario);
                revisarDocumentoBean.setLtsBotonesDoc(ltsBotones);
            }

            revisarDocumentoBean.setTamanioArchivo(String.valueOf(cydocConfig.getTamanioMaximoBytes()));
            revisarDocumentoBean.setTamanioArchivoAdjunto(obtenerMaximoTamanioArchivo());
        }

        return revisarDocumentoBean;
    }

    @Override
    public List<Boton> obtenerBotonAdendaAutomaticaPosicionContractual(Perfil perfilSesion, Character paraEstado, Boolean responsable, String parametro, Integer idRecurso) {
        return botonDAO.getBotonAdendaAutomaticaPosicionContractual(perfilSesion, Constantes.TIPO_DOCUMENTO_LEGAL, null, paraEstado, responsable, parametro, idRecurso);
    }

    @Override
    public List<Boton> obtenerBotonesDocumentoLegalExceptoParametro(Perfil perfilSesion, Character paraEstado, Boolean responsable, String parametro) {
        return botonDAO.buscarPorPerfilGridExceptoParametro(perfilSesion, Constantes.TIPO_DOCUMENTO_LEGAL, null, paraEstado, responsable, parametro);
    }

    @Override
    public List<Boton> obtenerBotonesDocumentoLegalExceptoParametroDocumento(Perfil perfilSesion, Character paraEstado, Boolean responsable, String parametro) {
        return botonDAO.buscarPorPerfilGridExceptoParametro(perfilSesion, "Documento", null, paraEstado, responsable, parametro);
    }

    @Override
    public List<Boton> obtenerBotones(Perfil perfil, Character paraEstado, Boolean responsable) {
        return botonDAO.buscarPorPerfilGrid(perfil, Constantes.GRID_DOCUMENTO, paraEstado);
    }
    private double obtenerMaximoTamanioArchivo() {
        double mb = cydocConfig.getTamanioMaximoBytes();
        LOGGER.info("tamanio de maximo en Bytes :" + mb);
        mb = (mb / 1024) / 1024;
        LOGGER.info("tamanio de maximo en MegaBytes :" + mb);
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(mb));
    }
    @Override
    public List<Archivo> obtenerArchivosxDocumento(Documento documento) {
        List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documento.getId());
        List<Version> versiones;

        for (Archivo archivo : archivos) {
            versiones = versionRepository.getVersions(archivo.getId());

            if (versiones == null || versiones.isEmpty()) {
                continue;
            }

            archivo.setVersion(versiones.get(0).getNumeroVersion());

            List<VersionArchivo> anteriores = new ArrayList<>();

            for (int i = 0; i < versiones.size(); i++) {
                Version version = versiones.get(i);
                VersionArchivo va  = new VersionArchivo();
                va.setNumeroVersion(version.getNumeroVersion());
                if (usuarioRepository.obtenerPorUsuario(version.getAutor()) != null) {
                    va.setAutor(usuarioRepository.obtenerPorUsuario(version.getAutor()).getLabel());
                } else {
                    va.setAutor("Migracion");
                }

                anteriores.add(va);
            }

            archivo.setVersiones(anteriores);
        }

        return archivos;
    }

    /**
     * Aplica validación para botón "Generar Plantilla" - Solo para Abogado, Abogado Responsable y Administrador
     */
    private List<Boton> aplicarValidacionGenerarPlantilla(List<Boton> botones, Usuario usuario) {
        if (botones == null || botones.isEmpty()) {
            return botones;
        }

        // Verificar si el usuario tiene los roles permitidos
        List<Rol> roles = rolRepository.buscarActivosPorUsuario(usuario.getId());
        boolean puedeGenerarPlantilla = false;
        
        if (roles != null && !roles.isEmpty()) {
            for (Rol rol : roles) {
                if (rol.getCodigo() != null && 
                    (Constantes.CODIGO_ROL_ABOGADO.equals(rol.getCodigo()) ||
                     Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE.equals(rol.getCodigo()) || 
                     Constantes.ROL_ADMINISTRADOR_CODIGO.equals(rol.getCodigo()))) {
                    puedeGenerarPlantilla = true;
                    break;
                }
            }
        }
        
        // Si no puede generar plantilla, filtrar los botones correspondientes
        if (!puedeGenerarPlantilla) {
            botones.removeIf(boton -> {
                String url = boton.getUrl();
                String parametroBoton = boton.getParametro();
                String nombreBoton = boton.getNombre();
                
                // Filtrar botones de "Generar Plantilla"
                return (url != null && url.toLowerCase().contains("generar") && 
                        (url.toLowerCase().contains("plantilla") || url.toLowerCase().contains("contrato") || url.toLowerCase().contains("adenda"))) ||
                       (parametroBoton != null && parametroBoton.toLowerCase().contains("generar") && 
                        (parametroBoton.toLowerCase().contains("plantilla") || parametroBoton.toLowerCase().contains("contrato") || parametroBoton.toLowerCase().contains("adenda"))) ||
                       (nombreBoton != null && nombreBoton.toLowerCase().contains("generar") && 
                        (nombreBoton.toLowerCase().contains("plantilla") || nombreBoton.toLowerCase().contains("contrato") || nombreBoton.toLowerCase().contains("adenda")));
            });
        }
        
        return botones;
    }
}