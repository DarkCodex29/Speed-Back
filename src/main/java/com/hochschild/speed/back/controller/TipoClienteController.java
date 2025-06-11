package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import com.hochschild.speed.back.model.filter.TipoClienteFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.TipoClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/tipoCliente")
public class TipoClienteController {

    private final TipoClienteService tipoClienteService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TipoClienteController(TipoClienteService tipoClienteService, JwtTokenUtil jwtTokenUtil) {
        this.tipoClienteService = tipoClienteService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoCliente>> find(TipoClienteFilter tipoClienteFilter, HttpServletRequest request) {
        List<TipoCliente> result = tipoClienteService.find(tipoClienteFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}