package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.mantenimiento.DataProcesoBean;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoProcesoService;
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
@RequestMapping("/proceso")
public class HcProcesoController {

    private final MantenimientoProcesoService mantenimientoProcesoService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public HcProcesoController(MantenimientoProcesoService mantenimientoProcesoService, JwtTokenUtil jwtTokenUtil) {
        this.mantenimientoProcesoService = mantenimientoProcesoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataProcesoBean>> find(ProcesoFilter procesoFilter, HttpServletRequest request) {
        List<DataProcesoBean> result = mantenimientoProcesoService.list(procesoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}