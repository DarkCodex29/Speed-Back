package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.RevisarBuscarDocumentoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */

@RestController
@RequestMapping("/revisarBuscarDocumento")
public class RevisarBuscarDocumentoController {
    private static final Logger LOGGER = Logger.getLogger(RevisarBuscarDocumentoController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarBuscarDocumentoService revisarBuscarDocumentoService;

    @Autowired
    public RevisarBuscarDocumentoController(JwtTokenUtil jwtTokenUtil,
                                            RevisarBuscarDocumentoService revisarBuscarDocumentoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.revisarBuscarDocumentoService = revisarBuscarDocumentoService;
    }

    @ResponseBody
    @GetMapping(value = "/{idExpediente}")
    public ResponseEntity<DetalleExpedienteBean> revisarBuscarDocumento(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = revisarBuscarDocumentoService.obtenerDetalleContrato2(idExpediente, idUsuario, idPerfil);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}