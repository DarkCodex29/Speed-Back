package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.BotonBean;
import com.hochschild.speed.back.model.bean.mantenimiento.BotonPerfilBean;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.filter.mantenimiento.BotonFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoBotonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/boton")
public class BotonController {
    private Logger LOGGER = LoggerFactory.getLogger(BotonController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoBotonService mantenimientoBotonService;

    public BotonController(JwtTokenUtil jwtTokenUtil,
                           MantenimientoBotonService mantenimientoBotonService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoBotonService = mantenimientoBotonService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BotonPerfilBean> find(@PathVariable("id") Integer id) {
        BotonPerfilBean result = mantenimientoBotonService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Boton>> list(@RequestBody BotonFilter botonFilter){
        List<Boton> result = mantenimientoBotonService.list(botonFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/perfilesDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Perfil>> perfilesDisponibles(@PathVariable("id") Integer id){
        List<Perfil> result = mantenimientoBotonService.listPerfilesDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody BotonBean botonBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoBotonService.save(botonBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}