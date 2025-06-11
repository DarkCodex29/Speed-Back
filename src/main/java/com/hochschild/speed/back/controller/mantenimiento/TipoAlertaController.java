package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.TipoAlertaBean;
import com.hochschild.speed.back.model.domain.speed.TipoAlerta;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoAlertaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoTipoAlertaService;
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
@RequestMapping("/tipoAlerta")
public class TipoAlertaController {
    private Logger LOGGER = LoggerFactory.getLogger(TipoAlertaController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoTipoAlertaService mantenimientoTipoAlertaService;

    public TipoAlertaController(JwtTokenUtil jwtTokenUtil, MantenimientoTipoAlertaService mantenimientoTipoAlertaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoTipoAlertaService = mantenimientoTipoAlertaService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoAlerta> find(@PathVariable("id") Integer id) {
        TipoAlerta result = mantenimientoTipoAlertaService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoAlerta>> list(@RequestBody TipoAlertaFilter tipoAlertaFilter){
        List<TipoAlerta> result = mantenimientoTipoAlertaService.list(tipoAlertaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody TipoAlertaBean tipoAlertaBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoTipoAlertaService.save(tipoAlertaBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}