package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.HcVisadoDao;
import com.hochschild.speed.back.model.bean.EnviarVisadoBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.EnviarVisadoService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EnviarVisadoServiceImpl implements EnviarVisadoService {

    private static final Logger LOGGER = Logger.getLogger(EnviarVisadoServiceImpl.class.getName());

    @Autowired
    private HcDocumentoLegalRepository hcDocumentoLegalRepository;

    @Autowired
    private EnviarNotificacionService enviarNotificacionService;

    @Autowired
    private HcVisadoRepository hcVisadoRepository;

    @Autowired
    private HcUsuarioPorVisadoRepository hcUsuarioPorVisadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionesConfig notificacionesConfig;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    @Autowired
    private CommonBusinessLogicService commonBusinessLogicService;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private ArchivoRepository archivoRepository;

    @Autowired
    private HcVisadoDao hcVisadoDao;

    @Override
    public ResponseModel enviarVisado(EnviarVisadoBean enviarVisadoBean, Integer idUsuario) {
        LOGGER.info("Entro");
        ResponseModel responseModel = new ResponseModel();
            Map<String, Object> mapTrazaGenerada = null;
            List<Usuario> destinatarios = new ArrayList<Usuario>();
            if (StringUtils.isEmpty(enviarVisadoBean.getObservacion())) {
                enviarVisadoBean.setObservacion(Constantes.OBS_HC_ENVIADO_VISADO);
            }
            try {

                Usuario remitente = usuarioRepository.findById(idUsuario);

                HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(enviarVisadoBean.getIdExpediente());

                /*Guardamos el Visado-----------------------------------------------------------------*/
                HcVisado visado = new HcVisado();
                HcVisado ultimoVisado = hcVisadoDao.obtenerUltimoVisado(documentoLegal.getId());

                visado.setDocumentoLegal(documentoLegal);
                visado.setFechaCreacion(new Date());
                visado.setObservacion(enviarVisadoBean.getObservacion());

                if (ultimoVisado != null) {//Hay mas de un visado
                    visado.setSecuencia(ultimoVisado.getSecuencia() + 1);

                } else {//Es el primer visado
                    visado.setSecuencia(1);
                }

                hcVisadoRepository.save(visado);

                documentoLegal.setEstado(Constantes.ESTADO_HC_ENVIADO_VISADO);
                hcDocumentoLegalRepository.save(documentoLegal);

                /*Guardamos los destinatarios en orden ------------------------------------------------*/
                int i = 1;
                for (Integer idVisador : enviarVisadoBean.getIdVisadores()) {
                    Usuario visador = usuarioRepository.findById(idVisador);
                    HcUsuarioPorVisadoPK usuarioVisadoPK = new HcUsuarioPorVisadoPK(visado, visador);
                    HcUsuarioPorVisado usuarioVisado = new HcUsuarioPorVisado();

                    usuarioVisado.setId(usuarioVisadoPK);
                    if (i == 1) {//Es el primer visador, guardamos la traza
                        usuarioVisado.setEstado(Constantes.ESTADO_ENVIADO);
                        mapTrazaGenerada = commonBusinessLogicService.generarTraza(enviarVisadoBean.getIdExpediente(), remitente, visador, enviarVisadoBean.getObservacion(), Constantes.ACCION_ENVIADO_VISADO);

                        destinatarios.add(visador);
                    } else {
                        usuarioVisado.setEstado(Constantes.ESTADO_PENDIENTE);
                    }
                    usuarioVisado.setOrden(i);

                    hcUsuarioPorVisadoRepository.save(usuarioVisado);

                    i++;
                }

                //Notificacion por correo al primer visador
                if (mapTrazaGenerada != null) {
                    TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getEnviarVisado());

                /*Solo se envia la notificacion al visador
				 * destinatarios.add(documentoLegal.getResponsable());
				if(documentoLegal.getResponsable().getId()!=documentoLegal.getSolicitante().getId()){
					destinatarios.add(documentoLegal.getSolicitante());
				}*/
                    Traza traza = (Traza) mapTrazaGenerada.get(Constantes.MAP_KEY_TRAZA);
                    traza.setPasoVisado(1);

                    enviarNotificacionService.registrarNotificacion(destinatarios, tipoNotificacion, traza);
                }

                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setMessage("Se envió a visado correctamente");
                return responseModel;

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
                responseModel.setMessage("Ocurrió un error al enviar a visado");
            }
        return responseModel;
    }

    @Override
    public ResponseModel validarArchivoPdf(Integer idExpediente){
        ResponseModel responseModel = new ResponseModel();
        if (validarArchivoPdfProcess(idExpediente)) {
            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setMessage("Se puede enviar a visado.");
        } else {
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage("Debe adjuntar un documento PDF.");
        }
        return responseModel;
    }

    @Override
    public List<InteresadoDTS> buscarVisadores(String term) {
        List<InteresadoDTS> interesadoDTS;

        if (!AppUtil.checkNullOrEmpty(term)) {
            term = "%" + AppUtil.quitarAcentos(term.toLowerCase().trim()) + "%";

            interesadoDTS = usuarioRepository.buscarInteresadosPorRol(term, Constantes.ESTADO_ACTIVO, Constantes.CODIGO_ROL_VISADOR);
            return interesadoDTS;
        }
        return null;
    }


    private Boolean validarArchivoPdfProcess(Integer idExpediente) {
        boolean result;
        try {


            HcDocumentoLegal documentoLegal = hcDocumentoLegalRepository.obtenerHcDocumentoLegalPorExpediente(idExpediente);
            boolean tieneArchivoPdf;
            int cntPdfs = 0;

            if (documentoLegal.getContrato() != null) {


                TipoDocumento tdContrato = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_CONTRATO);
                /*if(tdContrato==null){
                    tdContrato = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_CONTRATO);
                }*/
                List<Documento> documentos = documentoRepository.obtenerPorExpedienteYTipoDocumento(idExpediente, tdContrato.getId());

                LOGGER.debug("-----------------------------------------------------------------------------");
                LOGGER.debug("ES CONTRATO");
                LOGGER.debug("ID DEL DOCUMENTO = " + documentos.get(0).getId());
                LOGGER.debug("-----------------------------------------------------------------------------");

                List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documentos.get(0).getId());

                LOGGER.debug("-----------------------------------------------------------------------------");
                LOGGER.debug("ES CONTRATO");
                LOGGER.debug("CNT DE DOCUMENTOS = " + archivos.size());
                LOGGER.debug("-----------------------------------------------------------------------------");
                for (Archivo a : archivos) {

                    LOGGER.debug("NOMBRE DEL ARCHIVO EXISTENTE  = " + a.getNombre());
                    tieneArchivoPdf = a.getNombre().contains(".pdf");
                    LOGGER.debug("TIENE PDF? = " + tieneArchivoPdf);

                    if (tieneArchivoPdf) {
                        cntPdfs++;
                    }
                }
            } else if (documentoLegal.getAdenda() != null) {

                TipoDocumento tdAdenda = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_ADENDA);
               /*if(tdAdenda== null){
                    tdAdenda = tipoDocumentoRepository.obtenerTipoDocumentoPorNombre(Constantes.TIPO_DOCUMENTO_ADENDA);
               }*/
                List<Documento> documentos = documentoRepository.obtenerPorExpedienteYTipoDocumento(idExpediente, tdAdenda.getId());

                LOGGER.debug("-----------------------------------------------------------------------------");
                LOGGER.debug("ES ADENDA");
                LOGGER.debug("CNT DE DOCUMENTOS = " + documentos.size());
                LOGGER.debug("ID DEL DOCUMENTO = " + documentos.get(0).getId());
                LOGGER.debug("-----------------------------------------------------------------------------");

                List<Archivo> archivos = archivoRepository.obtenerPorDocumento(documentos.get(0).getId());

                for (Archivo a : archivos) {

                    LOGGER.debug("NOMBRE DEL ARCHIVO EXISTENTE  = " + a.getNombre());
                    tieneArchivoPdf = a.getNombre().contains(".pdf");
                    LOGGER.debug("TIENE PDF? = " + tieneArchivoPdf);

                    if (tieneArchivoPdf) {
                        cntPdfs++;
                    }
                }
            }

            LOGGER.debug("-----------------------------------------------------------------------------");
            LOGGER.debug("CNT DE PDFS = " + cntPdfs);
            LOGGER.debug("-----------------------------------------------------------------------------");

            result = cntPdfs != 0;

            return result;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

    }
}
