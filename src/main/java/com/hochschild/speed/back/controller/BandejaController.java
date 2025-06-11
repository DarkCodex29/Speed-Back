package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.BandejaBean;
import com.hochschild.speed.back.model.bean.MisPendientesBean;
import com.hochschild.speed.back.model.bean.SolicitudesPorEnviarBean;
import com.hochschild.speed.back.model.filter.bandejaEntrada.BandejaEntradaFilter;
import com.hochschild.speed.back.model.filter.bandejaMisSolicitudes.BandejaMisSolicitudesFilter;
import com.hochschild.speed.back.model.filter.bandejaPendientes.BandejaPendientesFilter;
import com.hochschild.speed.back.model.filter.bandejaSolicitudesPorEnviar.BandejaSolicitudesPorEnviarFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.BandejaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CAPSULA DIGITAL
 * @since 22/07/2023
 */
@RestController
@RequestMapping("/bandeja")
public class BandejaController {
    private final BandejaService bandejaService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public BandejaController(BandejaService bandejaService, JwtTokenUtil jwtTokenUtil, JwtTokenUtil jwtTokenUtil1) {
        this.bandejaService = bandejaService;
        this.jwtTokenUtil = jwtTokenUtil1;
    }

    @ResponseBody
    @RequestMapping(value = "/bandejaEntrada", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<BandejaBean>> bandejaEntrada(@RequestBody BandejaEntradaFilter bandejaEntradaFilter, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        bandejaEntradaFilter.setUsuario(idUsuario.toString());

        return bandejaService.obtenerBandejaEntrada(bandejaEntradaFilter);
    }

    @ResponseBody
    @RequestMapping(value = "/misPendientes", method = RequestMethod.POST)
    public ResponseEntity<List<MisPendientesBean>> misPendientes(@RequestBody BandejaPendientesFilter bandejaPendientesFilter, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        bandejaPendientesFilter.setUsuario(idUsuario.toString());

        return bandejaService.obtenerMisPendientes(bandejaPendientesFilter);
    }

    @ResponseBody
    @RequestMapping(value = "/misSolicitudes", method = RequestMethod.POST)
    public ResponseEntity<List<MisPendientesBean>> misSolicitudes(@RequestBody BandejaMisSolicitudesFilter bandejaMisSolicitudesFilter, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        bandejaMisSolicitudesFilter.setUsuario(idUsuario.toString());

        return bandejaService.obtenerMisSolicitudes(bandejaMisSolicitudesFilter);
    }

    @ResponseBody
    @RequestMapping(value = "/solicitudesPorEnviar", method = RequestMethod.POST)
    public ResponseEntity<List<SolicitudesPorEnviarBean>> obtenerSolicitudesPorEnviar(@RequestBody BandejaSolicitudesPorEnviarFilter bandejaSolicitudesPorEnviarFilter, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        bandejaSolicitudesPorEnviarFilter.setUsuario(idUsuario.toString());

        return bandejaService.obtenerSolicitudesPorEnviar(bandejaSolicitudesPorEnviarFilter);
    }
}