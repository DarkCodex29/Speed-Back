package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.NumeracionBean;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Numeracion;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.filter.mantenimiento.NumeracionFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoNumeracionService;
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
@RequestMapping("/numeracion")
public class NumeracionController {
    private Logger LOGGER = LoggerFactory.getLogger(NumeracionController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoNumeracionService mantenimientoNumeracionService;

    @Autowired
    public NumeracionController(JwtTokenUtil jwtTokenUtil,
                                MantenimientoNumeracionService mantenimientoNumeracionService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoNumeracionService = mantenimientoNumeracionService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Numeracion> find(@PathVariable("id") Integer id) {
        Numeracion result = mantenimientoNumeracionService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Numeracion>> list(@RequestBody NumeracionFilter numeracionFilter){
        List<Numeracion> result = mantenimientoNumeracionService.list(numeracionFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/tipoDocumentos", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoDocumento>> listTipos(){
        List<TipoDocumento> result = mantenimientoNumeracionService.listTipoDocumentos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/areas", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Area>> listAreas(){
        List<Area> result = mantenimientoNumeracionService.listAreas();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody NumeracionBean numeracionBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoNumeracionService.save(numeracionBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}