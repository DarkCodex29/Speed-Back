package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.TabModelBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.model.response.FilterTipoComponent;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.RevisarTrazaService;
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
@RequestMapping("/revisarExpediente")
public class RevisarExpedienteController {
    private static final Logger LOGGER = Logger.getLogger(RevisarExpedienteController.class.getName());
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarTrazaService revisarTrazaService;

    @Autowired
    public RevisarExpedienteController(JwtTokenUtil jwtTokenUtil,
                                       RevisarTrazaService revisarTrazaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.revisarTrazaService = revisarTrazaService;
    }

    @ResponseBody
    @GetMapping(value = "/revisarTraza/{idTraza}")
    public ResponseEntity<DetalleExpedienteBean> revisarExpedientePorTraza(@PathVariable("idTraza") Integer idTraza, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = revisarTrazaService.obtenerDetalleExpedientePorTraza(idTraza, idUsuario, idPerfil);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping(value = "/detalleExpediente/{idExpediente}")
    public ResponseEntity<DetalleExpedienteBean> revisarExpediente(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = revisarTrazaService.revisarExpedienteById(idExpediente, idUsuario, idPerfil);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/validateComponent/{idExpediente}")
    public ResponseEntity<FilterTipoComponent> validateComponent(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);
        return revisarTrazaService.filterExpedienteByStatus(idExpediente, idUsuario, idPerfil);
    }
    @ResponseBody
    @GetMapping(value = "/obtenerDatosBasicosExpediente/{idTraza}")
    public ResponseEntity<TabModelBean> obtenerDatosBasicosExpediente(@PathVariable("idTraza") Integer idTraza, HttpServletRequest request) {

        TabModelBean result = revisarTrazaService.obtenerDatosBasicosExpediente(idTraza);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}