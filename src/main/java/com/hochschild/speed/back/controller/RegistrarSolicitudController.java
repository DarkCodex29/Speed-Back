package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.GuardarExpedienteBean;
import com.hochschild.speed.back.model.bean.registrarContratoManual.DocumentoLegalManualBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.*;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.DocumentoLegalXAdendaDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */

@RestController
@RequestMapping("/registrarSolicitud")
public class RegistrarSolicitudController {
    private static final Logger LOGGER = Logger.getLogger(RegistrarSolicitudController.class.getName());
    private final RegistroSolicitudService registroSolicitudService;
    private final ExpedienteService expedienteService;
    private final RegistrarSolicitudService registrarSolicitudService;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final UsuarioService usuarioService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RegistrarSolicitudController(RegistroSolicitudService registroSolicitudService,
                                        ExpedienteService expedienteService,
                                        RegistrarSolicitudService registrarSolicitudService,
                                        CommonBusinessLogicService commonBusinessLogicService,
                                        UsuarioService usuarioService,
                                        JwtTokenUtil jwtTokenUtil) {
        this.registroSolicitudService = registroSolicitudService;
        this.expedienteService = expedienteService;
        this.registrarSolicitudService = registrarSolicitudService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.usuarioService = usuarioService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(value = "/buscarUsuarioSolicitante", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> buscarUsuarioSolicitante(HttpServletRequest request) {

        List<Usuario> result = registroSolicitudService.buscarUsuarioSolicitante(Constantes.CODIGO_ROL_SOLICITANTE);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/buscarUsuarioResponsable", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> buscarUsuarioResponsable(HttpServletRequest request) {

        List<Usuario> result = registroSolicitudService.buscarUsuarioResponsable(Constantes.CODIGO_ROL_ABOGADO_RESPONSABLE);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/buscarClienteContraparte", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente>> buscarClienteContraparte(HttpServletRequest request) {

        List<Cliente> result = registroSolicitudService.buscarClientesContraparte(Constantes.ESTADO_INACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/buscarClienteRepresentante", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente>> buscarClienteRepresentante(HttpServletRequest request) {

        List<Cliente> result = registroSolicitudService.buscarClientesRepresentanteLegal(Constantes.ESTADO_INACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/obtenerRepresentantes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente>> obtenerRepresentantes(@PathVariable("id") Integer idContraparte, HttpServletRequest request) {

        List<Cliente> result = registroSolicitudService.obtenerClientesRepresentantesContraparte(idContraparte);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/listarAreasPorCompania/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcArea>> listarAreasPorCompania(@PathVariable("id") Integer idCompania, HttpServletRequest request) {

        List<HcArea> result = registroSolicitudService.listarAreasPorCompania(idCompania, Constantes.ESTADO_ACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/obtenerUbicacionOperacionPorPorCompania/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcUbicacion>> obtenerUbicacionOperacionPorPorCompania(@PathVariable("id") Integer idCompania, HttpServletRequest request) {

        List<HcUbicacion> result = registroSolicitudService.obtenerUbicacionOperacionPorPorCompania(Constantes.TIPO_UBICACION_OPERACION, idCompania, Constantes.ESTADO_ACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/obtenerUbicacionOficinaPorPorCompania/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcUbicacion>> obtenerUbicacionOficinaPorPorCompania(@PathVariable("id") Integer idCompania, HttpServletRequest request) {

        List<HcUbicacion> result = registroSolicitudService.obtenerUbicacionOficinaPorPorCompania(Constantes.TIPO_UBICACION_OFICINA, idCompania, Constantes.ESTADO_ACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/obtenerUbicacionProyectoPorPorCompania/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcUbicacion>> obtenerUbicacionProyectoPorPorCompania(@PathVariable("id") Integer idCompania, HttpServletRequest request) {

        List<HcUbicacion> result = registroSolicitudService.obtenerUbicacionProyectoPorPorCompania(Constantes.TIPO_UBICACION_PROYECTO, idCompania, Constantes.ESTADO_ACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/buscarUbicacionExploracionPorPorCompania/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcUbicacion>> buscarUbicacionExploracionPorPorCompania(@PathVariable("id") Integer idCompania, HttpServletRequest request) {

        List<HcUbicacion> result = registroSolicitudService.obtenerUbicacionExploracionPorPorCompania(Constantes.TIPO_UBICACION_EXPLORACION, idCompania, Constantes.ESTADO_ACTIVO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/guardarExploracion", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarExploracion(@RequestBody ExploracionBean exploracionBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = expedienteService.guardarExploracion(exploracionBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @GetMapping(value = "/obtenerDatosSunat/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> obtenerDatosSunat(@PathVariable("id") Integer idCliente, HttpServletRequest request) {

        Cliente result = commonBusinessLogicService.obtenerDatosSunat(idCliente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoDocumentoAdendaAutomatica")
    public ResponseEntity<List<HcTipoContratoConfiguracion>> formularioAdendaAutomatica(HttpServletRequest request) {

        List<HcTipoContratoConfiguracion> result = registrarSolicitudService.buscarTipoAdenda(Constantes.CODIGO_TIPO_CONTRATO, Constantes.ESTADO_CONFIGURACION_HABILITADO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/datosContratoParaAdenda/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentoLegalXAdendaDTS> datosContratoParaAdenda(@PathVariable("id") Integer idDocumentoLegal, HttpServletRequest request) {

        DocumentoLegalXAdendaDTS result = registrarSolicitudService.datosContratoParaAdenda(idDocumentoLegal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/autocompletarContrato", method = RequestMethod.POST)
    public ResponseEntity<List<ContratoSumillaBean>> autocompletarContrato(@RequestBody AutoContratoBean autoContratoBean, HttpServletRequest request) {

        List<ContratoSumillaBean> result = registroSolicitudService.autocompletarContrato(autoContratoBean);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/getContratoSumilla/{idContrato}", method = RequestMethod.GET)
    public ResponseEntity<ContratoSumillaBean> getContratoSumillaById(@PathVariable("idContrato") Integer idContrato, HttpServletRequest request) {
        try {
            ContratoSumillaBean contratoSumillaBean = registroSolicitudService.getContratoSumillaById(idContrato);
            return new ResponseEntity<>(contratoSumillaBean, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/guardarExpediente", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarExpediente(@RequestBody GuardarExpedienteBean guardarExpedienteBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = expedienteService.guardarExpediente(guardarExpedienteBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @ResponseBody
    @RequestMapping(value = "/guardarDocumentoLegal", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarDocumentoLegal(@RequestBody DocumentoLegalBean documentoLegalBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        LOGGER.info(documentoLegalBean.getSumilla());
        ResponseModel responseModel = registroSolicitudService.guardarHcDocumentoLegal(documentoLegalBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @ResponseBody
    @RequestMapping(value = "/registrarDocumentoLegalManual", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registrarDocumentoLegalManual(@RequestBody DocumentoLegalManualBean documentoLegalManualBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = registrarSolicitudService.registrarHcDocumentoLegalManual(documentoLegalManualBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @ResponseBody
    @RequestMapping(value = "/guardarPenalidades", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarPenalidades(@RequestBody PenalidadesBean penalidadesBean) {

        ResponseModel responseModel = registroSolicitudService.guardarPenalidadesPorDocumentoLegal(penalidadesBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @ResponseBody
    @RequestMapping(value = "/registrarExpediente", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registrarExpediente(@RequestBody ExpedienteBean expedienteBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = registroSolicitudService.registrarExpediente(expedienteBean, idUsuario);
         return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @GetMapping(value = "/dialogSeleccionarUsuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> dialogSeleccionarUsuarios(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        Usuario result = usuarioService.dialogSeleccionarUsuarios(idUsuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/obtenerPenalidadesPorExpediente/{idExpediente}", method = RequestMethod.GET)
    public List<PenalidadBean> obtenerPenalidadesPorExpediente(@PathVariable("idExpediente") Integer idExpediente) {
        return registroSolicitudService.obtenerPenalidadesPorExpediente(idExpediente);
    }
}