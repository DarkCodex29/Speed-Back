package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.EstadoBean;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.EstadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CAPSULA DIGITAL
 * @since 24/07/2023
 */
@RestController
@RequestMapping("/estado")
public class EstadoController {

    private final JwtTokenUtil jwtTokenUtil;
    private final EstadoService estadoService;


    public EstadoController(JwtTokenUtil jwtTokenUtil, EstadoService estadoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.estadoService = estadoService;
    }

    @ResponseBody
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RequestMapping(value = "/obtenerEstados", method = RequestMethod.GET)
    public ResponseEntity<List<EstadoBean>> obtenerEstados(HttpServletRequest request) {

        List<EstadoBean> result = estadoService.obtenerEstados();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
