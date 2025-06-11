package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.revisarSolicitud.DetalleSolicitudEnviadaBean;
import com.hochschild.speed.back.model.bean.revisarSolicitud.SolicitudEnviadaBean;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.RevisarSolicitudEnviadaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */

@RestController
@RequestMapping("/revisarSolicitudEnviada")
public class RevisarSolicitudEnviadaController {
    private static final Logger LOGGER = Logger.getLogger(RevisarSolicitudEnviadaController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarSolicitudEnviadaService revisarSolicitudEnviadaService;

    @Autowired
    public RevisarSolicitudEnviadaController(JwtTokenUtil jwtTokenUtil,
                                             RevisarSolicitudEnviadaService revisarSolicitudEnviadaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.revisarSolicitudEnviadaService = revisarSolicitudEnviadaService;
    }

    @ResponseBody
    @GetMapping(value = "/detalleContratoAdenda")
    public ResponseEntity<DetalleSolicitudEnviadaBean> revisarSolicitudesPorEnviar(SolicitudEnviadaBean solicitudEnviadaBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        LOGGER.info("ID USUARIO " + idUsuario);
        
        DetalleSolicitudEnviadaBean result = revisarSolicitudEnviadaService.revisarSolicitudesPorEnviar(solicitudEnviadaBean, idUsuario);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}