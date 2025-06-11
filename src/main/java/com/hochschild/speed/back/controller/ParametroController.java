package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.filter.ParametroFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.ParametroService;
import com.hochschild.speed.back.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/parametro")
public class ParametroController {

    private final ParametroService parametroService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ParametroController(ParametroService parametroService, JwtTokenUtil jwtTokenUtil) {
        this.parametroService = parametroService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Parametro>> find(ParametroFilter parametroFilter, HttpServletRequest request) {
        List<Parametro> result = parametroService.find(parametroFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/{id}" )
    public ResponseEntity<Parametro> find(@PathVariable Integer id, HttpServletRequest request) {
        Parametro result = parametroService.buscarPorId(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/urlLegalAlDia" )
    public ResponseEntity<Parametro> find( HttpServletRequest request) {
        Parametro result = parametroService.buscarPorId(Constantes.ID_PARAMETRO_URL_LEGAL_AL_DIA);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}