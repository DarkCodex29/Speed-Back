package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.AceptarSolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/aceptarSolicitud")
public class AceptarSolicitudController {
    private final AceptarSolicitudService aceptarSolicitudService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AceptarSolicitudController(AceptarSolicitudService aceptarSolicitudService,
                                      JwtTokenUtil jwtTokenUtil) {
        this.aceptarSolicitudService = aceptarSolicitudService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value = "/{idExpediente}", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> aceptarSolicitud(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        ResponseModel responseModel = aceptarSolicitudService.aceptarSolicitud(idExpediente, jwtTokenUtil.getIdUserFromRequest(request));
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
