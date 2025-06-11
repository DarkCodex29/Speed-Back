package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.AreaBean;
import com.hochschild.speed.back.model.bean.mantenimiento.AreaPadreBean;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.model.filter.mantenimiento.AreaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoAreaService;
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
@RequestMapping("/area")
public class AreaController {
    private Logger LOGGER = LoggerFactory.getLogger(AreaController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoAreaService mantenimientoAreaService;

    public AreaController(JwtTokenUtil jwtTokenUtil,
                          MantenimientoAreaService mantenimientoAreaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoAreaService = mantenimientoAreaService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaPadreBean> find(@PathVariable("id") Integer id) {
        AreaPadreBean result = mantenimientoAreaService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AreaPadreBean>> list(@RequestBody AreaFilter areaFilter){
        List<AreaPadreBean> result = mantenimientoAreaService.list(areaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/sedes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sede>> listSedes(){
        List<Sede> result = mantenimientoAreaService.buscarSedes();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/dependencias", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Area>> listDependencias(){
        List<Area> result = mantenimientoAreaService.buscarAreas();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody AreaBean areaBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoAreaService.save(areaBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}