package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosAdendaBean;
import com.hochschild.speed.back.model.filter.bandejaEntrada.AdendaFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.DatosAdendaService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FERNANDO SALVI
 * @since 03/08/2023
 */

@RestController
@RequestMapping("/datosAdenda")
public class DatosAdendaController {

    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger LOGGER = Logger.getLogger(DatosAdendaController.class.getName());
    private final DatosAdendaService datosAdendaService;

    public DatosAdendaController(JwtTokenUtil jwtTokenUtil,
                                 DatosAdendaService datosAdendaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.datosAdendaService = datosAdendaService;
    }

    @GetMapping(value = "/botonDatosAdenda", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<DatosAdendaBean> botonDatosAdenda(AdendaFilter adendaFilter, HttpServletRequest request) {

        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));

        DatosAdendaBean result = datosAdendaService.botonDatosAdenda(adendaFilter, idUsuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}