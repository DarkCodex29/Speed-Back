package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.SedeBean;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.domain.speed.Ubigeo;
import com.hochschild.speed.back.model.filter.mantenimiento.SedeFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoSedeService;
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
@RequestMapping("/sede")
public class SedeController {
    private Logger LOGGER = LoggerFactory.getLogger(SedeController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoSedeService mantenimientoSedeService;

    public SedeController(JwtTokenUtil jwtTokenUtil, MantenimientoSedeService mantenimientoSedeService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoSedeService = mantenimientoSedeService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sede> find(@PathVariable("id") Integer id) {
        Sede result = mantenimientoSedeService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sede>> list(@RequestBody SedeFilter sedeFilter){
        List<Sede> result = mantenimientoSedeService.list(sedeFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/departamentos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ubigeo>> departamentos(){
        List<Ubigeo> result = mantenimientoSedeService.listDepartamentos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/provincias/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ubigeo>> provincias(@PathVariable("id") Integer id){
        List<Ubigeo> result = mantenimientoSedeService.listProvincias(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/distritos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ubigeo>> distritos(@PathVariable("id") Integer id){
        List<Ubigeo> result = mantenimientoSedeService.listDistritos(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody SedeBean sedeBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoSedeService.save(sedeBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}