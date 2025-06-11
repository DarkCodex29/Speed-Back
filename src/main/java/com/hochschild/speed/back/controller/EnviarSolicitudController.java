package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.EnviarSolicitudService;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/enviarSolicitud")
public class EnviarSolicitudController {
    private static final Logger LOGGER = Logger.getLogger(EnviarSolicitudController.class.getName());
    private final EnviarSolicitudService enviarSolicitudService;
    private final JwtTokenUtil jwtTokenUtil;

    public EnviarSolicitudController(EnviarSolicitudService enviarSolicitudService,
                                     JwtTokenUtil jwtTokenUtil) {
        this.enviarSolicitudService = enviarSolicitudService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/{idExpediente}", consumes = MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> enviarSolicitud(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = enviarSolicitudService.enviarSolicitud(idExpediente, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
