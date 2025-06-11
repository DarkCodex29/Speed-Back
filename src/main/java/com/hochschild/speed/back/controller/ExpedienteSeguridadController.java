package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.utils.SeguridadConfigDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.ExpedienteSeguridadService;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/expedienteSeguridad")
public class ExpedienteSeguridadController {

    private static final Logger LOGGER = Logger.getLogger(ExpedienteSeguridadController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;

    private final ExpedienteSeguridadService expedienteSeguridadService;

    public ExpedienteSeguridadController(JwtTokenUtil jwtTokenUtil, ExpedienteSeguridadService expedienteSeguridadService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.expedienteSeguridadService = expedienteSeguridadService;
    }

    @RequestMapping(value = "verificarPermisos/{idExpediente}", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> aceptarSolicitud(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        ResponseModel responseModel = expedienteSeguridadService.verificarPermisos(idExpediente, idUsuario);

        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseModel> guardar(@RequestBody SeguridadConfigDTO seguridadConfigDTO, HttpServletRequest request) {
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        ResponseModel responseModel = expedienteSeguridadService.guardar(seguridadConfigDTO.getEsConfidencial(), seguridadConfigDTO.getIdExpediente(), idUsuario, seguridadConfigDTO.getUsuarios());
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
