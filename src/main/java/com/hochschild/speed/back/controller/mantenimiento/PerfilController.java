package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.PerfilBean;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.filter.mantenimiento.PerfilFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoPerfilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/perfil")
public class PerfilController {
    private Logger LOGGER = LoggerFactory.getLogger(PerfilController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoPerfilesService mantenimientoPerfilesService;

    @Autowired
    public PerfilController(JwtTokenUtil jwtTokenUtil, MantenimientoPerfilesService mantenimientoPerfilesService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoPerfilesService = mantenimientoPerfilesService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Perfil> find(@PathVariable("id") Integer id) {
        Perfil result = mantenimientoPerfilesService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Perfil>> list(@RequestBody PerfilFilter perfilFilter){
        List<Perfil> result = mantenimientoPerfilesService.list(perfilFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody PerfilBean perfilBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoPerfilesService.save(perfilBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}