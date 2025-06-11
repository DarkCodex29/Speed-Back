package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.adjuntarDocumento.AdjuntarDocumentoModalBean;
import com.hochschild.speed.back.model.bean.adjuntarDocumento.NumeracionPorTipoBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.AdjuntarDocumentoBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.AdjuntarDocumentoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/adjuntarDocumento")
public class AdjuntarDocumentoController {
    private static final Logger LOGGER = Logger.getLogger(AdjuntarDocumentoController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;

    private final AdjuntarDocumentoService adjuntarDocumentoService;

    @Autowired
    public AdjuntarDocumentoController(AdjuntarDocumentoService adjuntarDocumentoService,
                                       JwtTokenUtil jwtTokenUtil) {
        this.adjuntarDocumentoService = adjuntarDocumentoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardar(@RequestBody AdjuntarDocumentoBean adjuntarDocumentoBean, HttpServletRequest request) {
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));

        ResponseModel responseModel = adjuntarDocumentoService.guardarDocumentoGeneral(adjuntarDocumentoBean, idUsuario, request);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @ResponseBody
    @RequestMapping(value = "/guardarNuevoDocumento", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarNuevoDocumento(@RequestBody AdjuntarDocumentoBean adjuntarDocumentoBean, HttpSession sesion, HttpServletRequest request) {
        LOGGER.debug("Dentro del controlador guardar Nuevo Documento");
        LOGGER.debug("ID EXPEDIENTE DOCUMENTO: " + adjuntarDocumentoBean.getIdExpediente());
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));

        ResponseModel responseModel = adjuntarDocumentoService.guardarNuevoDocumento(adjuntarDocumentoBean, idUsuario, sesion, request);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @RequestMapping(value = "/adjuntarDocumentoExpediente", method = RequestMethod.GET)
    public ResponseEntity<AdjuntarDocumentoModalBean> adjuntarDocumentoExpediente(HttpSession sesion, HttpServletRequest request) {
        AdjuntarDocumentoModalBean responseModel = adjuntarDocumentoService.adjuntarDocumentoModal();
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/numeracion/{idTipoDocumento}", method = RequestMethod.GET)
    public ResponseEntity<NumeracionPorTipoBean> numeracioPorTipo(@PathVariable Integer idTipoDocumento, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        NumeracionPorTipoBean responseModel = adjuntarDocumentoService.informacionNumeracion(idUsuario, idTipoDocumento);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}