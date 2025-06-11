package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoBean;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoTipoProcesoService;
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
@RequestMapping("/tipoProceso")
public class TipoProcesoController {
    private Logger LOGGER = LoggerFactory.getLogger(TipoProcesoController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoTipoProcesoService mantenimientTipoProcesoService;
    public TipoProcesoController(JwtTokenUtil jwtTokenUtil, MantenimientoTipoProcesoService mantenimientTipoProcesoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientTipoProcesoService = mantenimientTipoProcesoService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Proceso> find(@PathVariable("id") Integer id) {
        Proceso result = mantenimientTipoProcesoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Proceso>> list(ProcesoFilter procesoFilter){
        List<Proceso> result = mantenimientTipoProcesoService.list(procesoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody ProcesoBean procesoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientTipoProcesoService.save(procesoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}