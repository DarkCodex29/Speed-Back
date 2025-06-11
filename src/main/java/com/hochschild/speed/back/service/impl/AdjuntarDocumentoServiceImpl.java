package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.BotonDAO;
import com.hochschild.speed.back.model.bean.adjuntarDocumento.AdjuntarDocumentoModalBean;
import com.hochschild.speed.back.model.bean.adjuntarDocumento.NumeracionPorTipoBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.AdjuntarDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.AdjuntarArchivoService;
import com.hochschild.speed.back.service.AdjuntarDocumentoService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.*;

@Service("AdjuntarDocumentoService")
public class AdjuntarDocumentoServiceImpl implements AdjuntarDocumentoService {
    private static final Logger LOGGER = Logger.getLogger(AdjuntarDocumentoServiceImpl.class.getName());

    private final UsuarioRepository usuarioRepository;
    private final AdjuntarArchivoService adjuntarArchivoService;
    private final ArchivoRepository archivoRepository;
    private final DocumentoRepository documentoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final CampoRepository campoRepository;
    private final NumeracionRepository numeracionRepository;
    private final CampoPorDocumentoRepository campoPorDocumentoRepository;
    private final CydocConfig cydocConfig;
    private final BotonDAO botonDAO;

    @Autowired
    public AdjuntarDocumentoServiceImpl(
            UsuarioRepository usuarioRepository,
            AdjuntarArchivoService adjuntarArchivoService,
            ArchivoRepository archivoRepository,
            DocumentoRepository documentoRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            CampoRepository campoRepository,
            NumeracionRepository numeracionRepository,
            CampoPorDocumentoRepository campoPorDocumentoRepository,
            CydocConfig cydocConfig,
            BotonDAO botonDAO
    ) {
        this.usuarioRepository = usuarioRepository;
        this.adjuntarArchivoService = adjuntarArchivoService;
        this.archivoRepository = archivoRepository;
        this.documentoRepository = documentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.campoRepository = campoRepository;
        this.numeracionRepository = numeracionRepository;
        this.campoPorDocumentoRepository = campoPorDocumentoRepository;
        this.cydocConfig = cydocConfig;
        this.botonDAO = botonDAO;
    }

    @Override
    public Integer guardarDocumento(Documento documento, Map<String, String> map) {

        //Documento documentoBase=documentoDAO.get(documento.getId());
        documento.setFechaCreacion(new Date());
        documento.setEstado(Constantes.ESTADO_ACTIVO);

        // Enumerar
        if (documento.getNumero() != null) {
            if (documento.getNumero().equals("-1")) {

                Numeracion numeracion = numeracionRepository.numeracionPorAreayTipoDocumento(documento.getAutor().getArea().getId(), documento.getTipoDocumento().getId());
                numeracion.setValor(numeracion.getValor() + 1);
                numeracion.setLongitud((numeracion.getLongitud() != null) ? numeracion.getLongitud() : 1);
                numeracionRepository.save(numeracion);
                String logitud = "%0" + numeracion.getLongitud() + "d";
                String valor = String.format(logitud, numeracion.getValor());
                documento.setNumero(numeracion.getPreformato() + valor + numeracion.getPostFormato());
            } else if (documento.getNumero().equals("ND") || documento.getNumero().equals("SN")) {
                documento.setNumero("S/N");
            }
        } else {
            documento.setNumero(Constantes.NUMERACION_AUTOMATICA_NO_GENERADA);
        }

        documentoRepository.save(documento);

        LOGGER.info("id :" + documento.getId());

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(documento.getTipoDocumento().getId());
        List<Campo> listaCampos = campoRepository.getCamposPorTipoDocumento(tipoDocumento.getId());

        LOGGER.info("lista de campos :" + listaCampos.size());

        for (Campo campo : listaCampos) {
            if (map != null) {
                Set<String> llaves = map.keySet();
                for (String llave : llaves) {
                    if (llave.equals(campo.getNombre())) {
                        CampoPorDocumento existente = campoPorDocumentoRepository.obtenerCampoPorDocumento(documento.getId(), campo.getId());
                        if (existente != null) {
                            existente.setValor(map.get(llave));
                            campoPorDocumentoRepository.save(existente);
                        } else {
                            CampoPorDocumento campoPorDocumento = new CampoPorDocumento(campo, documento);
                            campoPorDocumento.setValor(map.get(llave));
                            campoPorDocumentoRepository.save(campoPorDocumento);
                        }

                    }
                }
            }
        }
        return documento.getId();
    }

    @Override
    public Integer guardarNuevoDocumento(Documento documento, Map<String, String> map) {

        documento.setFechaCreacion(new Date());
        documento.setEstado(Constantes.ESTADO_ACTIVO);
        documentoRepository.save(documento);

        if (documento.getNumero() != null) {
            if (!documento.getNumero().equals("-1") & !documento.getNumero().equals("SN") & !documento.getNumero().equals("ND")) {

            } else if (documento.getNumero().equals("-1")) {
                // Numeracion Automatica
                Numeracion numeracion = numeracionRepository.numeracionPorAreayTipoDocumento(documento.getAutor().getArea().getId(), documento.getTipoDocumento().getId());
                numeracion.setValor(numeracion.getValor() + 1);
                numeracion.setLongitud((numeracion.getLongitud() != null) ? numeracion.getLongitud() : 1);
                numeracionRepository.save(numeracion);
                String logitud = "%0" + numeracion.getLongitud() + "d";
                String valor = String.format(logitud, numeracion.getValor());
                documento.setNumero(numeracion.getPreformato() + valor + numeracion.getPostFormato());
            } else if (documento.getNumero().equals("ND") || documento.getNumero().equals("SN")) {
                documento.setNumero("S/N");
            }
        } else {
            documento.setNumero(Constantes.NUMERACION_AUTOMATICA_NO_GENERADA);
        }

        LOGGER.info("id :" + documento.getId());

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(documento.getTipoDocumento().getId());
        List<Campo> listaCampos = campoRepository.getCamposPorTipoDocumento(tipoDocumento.getId());

        LOGGER.info("lista de campos :" + listaCampos.size());

        for (Campo campo : listaCampos) {

            Set<String> llaves = map.keySet();
            for (String llave : llaves) {
                if (llave.equals(campo.getNombre())) {
                    CampoPorDocumento campoPorDocumento = new CampoPorDocumento(campo, documento);
                    campoPorDocumento.setValor(map.get(llave));
                    campoPorDocumentoRepository.save(campoPorDocumento);
                }

            }

        }

        return documento.getId();
    }

    @Override
    public Boolean borrarArchivosDocumento(Documento documento) {

        List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documento.getId());

        if (archivos != null && !archivos.isEmpty()) {
            for (Archivo a : archivos) {
                archivoRepository.delete(a.getId());
            }
        }
        return true;
    }

    @Override
    public ResponseModel guardarDocumentoGeneral(AdjuntarDocumentoBean adjuntarDocumentoBean, Integer idUsuario, HttpServletRequest request) {

        ResponseModel responseModel = new ResponseModel();

        LOGGER.info("========================================================");
        LOGGER.info("GUARDAR DOCUMENTO GENERAL");
        LOGGER.info("========================================================");

        Documento documento = new Documento();
        TipoDocumento tipoDocumento = new TipoDocumento();
        Expediente expediente = new Expediente();

        documento.setId(adjuntarDocumentoBean.getId());
        tipoDocumento.setId(adjuntarDocumentoBean.getIdTipoDocumento());
        documento.setTipoDocumento(tipoDocumento);
        expediente.setId(adjuntarDocumentoBean.getIdExpediente());
        documento.setExpediente(expediente);
        documento.setTitulo(adjuntarDocumentoBean.getTitulo());

        Map<String, String[]> map;
        if(request==null){
            map= new HashMap<>();
        }else{
            map = request.getParameterMap();
        }
        Map<String, String> mapa = new HashMap<>();
        Set<String> keys = map.keySet();
        for (String k : keys) {
            mapa.put(k, map.get(k)[0]);
        }
        int id = 0;
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario != null) {
            if (documento.getAutor() == null) {
                documento.setAutor(usuario);
            }
            LOGGER.debug("id Guardado :" + id);
            if (documento.getId() != null) {
                borrarArchivosDocumento(documento);
            }
            if (adjuntarDocumentoBean.getArchivo() != null) {
                for (String arch : adjuntarDocumentoBean.getArchivo()) {
                    LOGGER.info("archivo :" + arch);
                    Integer fila = null;
                    if (arch.length() == arch.lastIndexOf('-') + 2) {
                        fila = Integer.parseInt(arch.substring(arch.lastIndexOf('-') + 1));
                        arch = arch.substring(0, arch.lastIndexOf('-'));
                    }

                    Archivo archivoSubir = null;
                    try {
                        LOGGER.info(documento.getId());
                        LOGGER.info(arch);
                        if (documento.getId() != null) {
                            guardarDocumento(documento, mapa);
                            archivoSubir = adjuntarArchivoService.subirArchivo(documento, arch, fila, usuario, mapa);
                        } else {
                            guardarNuevoDocumento(documento, mapa);
                            archivoSubir = adjuntarArchivoService.subirArchivo(documento, arch, fila, usuario, mapa);
                        }

                        LOGGER.info(archivoSubir);
                        id = documento.getId();
                    } catch (ExcepcionAlfresco e) {
                        LOGGER.info("No se pudo subir el archivo.", e);
                        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        responseModel.setMessage("error");
                        return responseModel;
                    }
                }

            } else {
                if (documento.getId() != null) {
                    guardarDocumento(documento, mapa);

                } else {
                    guardarNuevoDocumento(documento, mapa);
                }
                responseModel.setId(documento.getId());
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("exito");
                return responseModel;
            }
            if (id > 0) {
                responseModel.setId(id);
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("exito");
                return responseModel;
            }
        }
        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        responseModel.setMessage("Error");
        return responseModel;
    }

    @Override
    public AdjuntarDocumentoModalBean adjuntarDocumentoModal() {
        AdjuntarDocumentoModalBean modal = new AdjuntarDocumentoModalBean();
        modal.setTipos(getTipos());
        double mb = cydocConfig.getTamanioMaximoBytes();
        LOGGER.info("tamanio de maximo en Bytes :" + mb);
        mb = (mb / 1024) / 1024;
        LOGGER.info("tamanio de maximo en MegaBytes :" + mb);
        DecimalFormat df = new DecimalFormat("#.##");
        modal.setTamanioArchivoAdjunto(Double.parseDouble(df.format(mb)));
        modal.setTamanioArchivo(String.valueOf(mb));
        return modal;
    }

    @Override
    public List<TipoDocumento> getTipos() {
        return tipoDocumentoRepository.getTiposActivos();
    }

    @Override
    public List<Boton> obtenerBotonesPorGrid(Perfil perfil, String codigoGrid) {
        LOGGER.info("El id del perfil es :" + perfil.getId());
        return botonDAO.buscarPorPerfilGrid(perfil, codigoGrid);
    }

    @Override
    public NumeracionPorTipoBean informacionNumeracion(Integer idUsuario, Integer idTipoDocumento) {
        NumeracionPorTipoBean numeracionPorTipoBean = new NumeracionPorTipoBean();
        Usuario usuario = usuarioRepository.obtenerPorId(idUsuario);
        try {
            Numeracion num = numeracionRepository.numeracionPorAreayTipoDocumento(usuario.getArea().getId(), idTipoDocumento);
            numeracionPorTipoBean.setTipo(num.getTipo());
            numeracionPorTipoBean.setValor(num.getValor().toString());
            numeracionPorTipoBean.setHttpSatus(HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            numeracionPorTipoBean.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            numeracionPorTipoBean.setMessage("error");
        }

        return numeracionPorTipoBean;
    }

    @Override
    public ResponseModel guardarNuevoDocumento(AdjuntarDocumentoBean adjuntarDocumentoBean, Integer idUsuario, HttpSession sesion, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();

        LOGGER.info("========================================================");
        LOGGER.info("GUARDAR NUEVO DOCUMENTO");
        LOGGER.info("========================================================");

        Documento documento = new Documento();
        TipoDocumento tipoDocumento = new TipoDocumento();
        Expediente expediente = new Expediente();

        documento.setId(adjuntarDocumentoBean.getId());
        tipoDocumento.setId(adjuntarDocumentoBean.getIdTipoDocumento());
        documento.setTipoDocumento(tipoDocumento);
        expediente.setId(adjuntarDocumentoBean.getIdExpediente());
        documento.setExpediente(expediente);
        documento.setTitulo(adjuntarDocumentoBean.getTitulo());
        documento.setNumero(adjuntarDocumentoBean.getNumero());

        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> mapa = new HashMap<String, String>();
        Map<Object, Object> respuesta = new HashMap<Object, Object>();
        Set<String> keys = map.keySet();
        for (String k : keys) {
            mapa.put(k, map.get(k)[0]);
        }
        int id = 0;
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario != null) {
            if (documento.getAutor() == null) {
                documento.setAutor(usuario);
            }

            guardarNuevoDocumento(documento, mapa);
            id = documento.getId();

            LOGGER.debug("id Expediente :" + documento.getExpediente().getId());
            LOGGER.debug("id Guardado :" + id);

            if (adjuntarDocumentoBean.getArchivo() != null) {
                for (String arch : adjuntarDocumentoBean.getArchivo()) {
                    LOGGER.info("archivo :" + arch);
                    Archivo archivoSubir = null;
                    try {
                        archivoSubir = adjuntarArchivoService.subirArchivo(documento, arch, null, usuario, mapa);
                        id = archivoSubir.getId();
                    } catch (ExcepcionAlfresco e) {
                        LOGGER.info("No se pudo subir el archivo.", e);
                        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        responseModel.setMessage("Error");
                        return responseModel;
                    }
                }
            }
            LOGGER.info("El id del documento guardado  es :" + id);

            if (id > 0) {
                responseModel.setId(id);
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("El documento fue adjuntado satisfactoriamente.");
                return responseModel;
            }
        }
        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        responseModel.setMessage("Error");

        return responseModel;
    }

    @Override
    public ResponseModel eliminarArchivoPorId(Integer[] idArchivos, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();
        try {
            if(idUsuario != null) {
                for(Integer idArchivo : idArchivos){
                    Archivo archivo = archivoRepository.findById(idArchivo);
                    archivo.setEstado(Constantes.ESTADO_INACTIVO);
                    archivoRepository.save(archivo);
                }
                responseModel.setId(null);
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("Operación exitosa");
            }
        } catch (Exception ex) {
            responseModel.setId(null);
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Operación fallida");
            LOGGER.error(ex.getMessage(), ex);
        }
        return responseModel;
    }
}