package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.AlertaBean;
import com.hochschild.speed.back.model.bean.mantenimiento.AlertaCleanBean;
import com.hochschild.speed.back.model.domain.speed.Grid;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.filter.mantenimiento.AlertaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoAlertaService;
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
@RequestMapping("/alerta")
public class AlertaController {
    private Logger LOGGER = LoggerFactory.getLogger(AlertaController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoAlertaService mantenimientoAlertaService;

    public AlertaController(JwtTokenUtil jwtTokenUtil, MantenimientoAlertaService mantenimientoAlertaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoAlertaService = mantenimientoAlertaService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlertaCleanBean> find(@PathVariable("id") Integer id) {
        AlertaCleanBean result = mantenimientoAlertaService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AlertaCleanBean>> list(@RequestBody AlertaFilter alertaFilter){
        List<AlertaCleanBean> result = mantenimientoAlertaService.list(alertaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/grids", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Grid>> listGrids(){
        List<Grid> result = mantenimientoAlertaService.listGrids();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoAlertas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoAlerta>> listTipoAlertas(){
        List<TipoAlerta> result = mantenimientoAlertaService.listTipoAlertas();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody AlertaBean alertaBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoAlertaService.save(alertaBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}