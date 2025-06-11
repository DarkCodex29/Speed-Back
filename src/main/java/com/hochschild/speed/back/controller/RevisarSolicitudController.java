package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.RevisarSolicitudService;
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
@RequestMapping("/revisarSolicitud")
public class RevisarSolicitudController {
    private static final Logger LOGGER = Logger.getLogger(RevisarSolicitudController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarSolicitudService revisarSolicitudService;

    @Autowired
    public RevisarSolicitudController(JwtTokenUtil jwtTokenUtil,
                                      RevisarSolicitudService revisarSolicitudService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.revisarSolicitudService = revisarSolicitudService;
    }


    @ResponseBody
    @GetMapping(value = "/{idExpediente}")
    public ResponseEntity<DetalleExpedienteBean> revisarSolicitud(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = revisarSolicitudService.obtenerDetalleContrato2(idExpediente, idUsuario, idPerfil);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}