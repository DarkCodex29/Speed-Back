package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.RolBean;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.filter.mantenimiento.RolFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoRolesService;
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
@RequestMapping("/rol")
public class RolController {
    private Logger LOGGER = LoggerFactory.getLogger(RolController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoRolesService mantenimientoRolesService;

    @Autowired
    public RolController(JwtTokenUtil jwtTokenUtil,
                         MantenimientoRolesService mantenimientoRolesService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoRolesService = mantenimientoRolesService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rol> find(@PathVariable("id") Integer id) {
        Rol result = mantenimientoRolesService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> list(@RequestBody RolFilter rolFilter){
        List<Rol> result = mantenimientoRolesService.list(rolFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody RolBean rolBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoRolesService.save(rolBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}