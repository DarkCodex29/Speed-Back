package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.reabrirExpediente.ReabrirBean;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.ReabrirExpedienteService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/reabrirExpediente")
public class ReabrirExpedienteController {
    private static final Logger LOGGER = Logger.getLogger(ReabrirExpedienteController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final ReabrirExpedienteService reabrirExpedienteService;

    public ReabrirExpedienteController(JwtTokenUtil jwtTokenUtil, ReabrirExpedienteService reabrirExpedienteService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.reabrirExpedienteService = reabrirExpedienteService;
    }

    @GetMapping(value = "/abogadosResponsables", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorAlarma() {

        List<Usuario> result = reabrirExpedienteService.abogadosResponsables();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/reabrir", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> registrarExpediente(@RequestBody ReabrirBean reabrirBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = reabrirExpedienteService.reabrirExpediente(reabrirBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
