package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosContratoBean;
import com.hochschild.speed.back.model.bean.registrarSolicitud.DocumentoLegalBean;
import com.hochschild.speed.back.model.filter.bandejaEntrada.ContratoBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.DatosContratoService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FERNANDO SALVI
 * @since 03/08/2023
 */

@RestController
@RequestMapping("/datosContrato")
public class DatosContratoController {

    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger LOGGER = Logger.getLogger(DatosContratoController.class.getName());
    private final DatosContratoService datosContratoService;

    public DatosContratoController(JwtTokenUtil jwtTokenUtil,
                                   DatosContratoService datosContratoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.datosContratoService = datosContratoService;
    }

    @ResponseBody
    @GetMapping(value = "/botonDatosContrato")
    public ResponseEntity<DatosContratoBean> botonDatosContrato(@RequestParam(name = "idExpediente") int idExpediente, HttpServletRequest request) {
    //public ResponseEntity<DatosContratoBean> botonDatosContrato(@RequestBody ContratoBean contratoBean, HttpServletRequest request) {
        ContratoBean contratoBean = new ContratoBean();
        contratoBean.setIdExpediente(idExpediente);
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        DatosContratoBean result = datosContratoService.botonDatosContrato(contratoBean, idUsuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/guardarDocumentoLegal", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarDocumentoLegal(@RequestBody DocumentoLegalBean documentoLegalBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = datosContratoService.guardarHcDocumentoLegal(documentoLegalBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}