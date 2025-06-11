package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.TipoNotificacionBean;
import com.hochschild.speed.back.model.domain.speed.TipoNotificacion;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoNotificacionFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoTipoNotificacionService;
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
@RequestMapping("/tipoNotificacion")
public class TipoNotificacionController {
    private Logger LOGGER = LoggerFactory.getLogger(TipoNotificacionController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoTipoNotificacionService mantenimientoTipoNotificacionService;

    public TipoNotificacionController(JwtTokenUtil jwtTokenUtil,
                                      MantenimientoTipoNotificacionService mantenimientoTipoNotificacionService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoTipoNotificacionService = mantenimientoTipoNotificacionService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoNotificacion> find(@PathVariable("id") Integer id) {
        TipoNotificacion result = mantenimientoTipoNotificacionService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoNotificacion>> list(@RequestBody TipoNotificacionFilter tipoNotificacionFilter){
        List<TipoNotificacion> result = mantenimientoTipoNotificacionService.list(tipoNotificacionFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody TipoNotificacionBean tipoNotificacionBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoTipoNotificacionService.save(tipoNotificacionBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}