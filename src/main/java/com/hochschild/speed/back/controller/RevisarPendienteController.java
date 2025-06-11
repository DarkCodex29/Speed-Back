package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.RevisarPendienteService;
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
@RequestMapping("/revisarPendiente")
public class RevisarPendienteController {
    private static final Logger LOGGER = Logger.getLogger(RevisarPendienteController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarPendienteService revisarPendienteService;

    @Autowired
    public RevisarPendienteController(JwtTokenUtil jwtTokenUtil,
                                      RevisarPendienteService revisarPendienteService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.revisarPendienteService = revisarPendienteService;
    }


    @ResponseBody
    @GetMapping(value = "/{idExpediente}")
    public ResponseEntity<DetalleExpedienteBean> revisarPendiente(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = revisarPendienteService.obtenerDetalleContrato(idExpediente, idUsuario, idPerfil);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}