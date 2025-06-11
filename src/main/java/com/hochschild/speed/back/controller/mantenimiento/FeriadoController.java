package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.FeriadoBean;
import com.hochschild.speed.back.model.domain.speed.Feriado;
import com.hochschild.speed.back.model.filter.mantenimiento.FeriadoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoFeriadoService;
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
@RequestMapping("/feriado")
public class FeriadoController {
    private Logger LOGGER = LoggerFactory.getLogger(FeriadoController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoFeriadoService mantenimientoFeriadoService;

    public FeriadoController(JwtTokenUtil jwtTokenUtil,
                             MantenimientoFeriadoService mantenimientoFeriadoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoFeriadoService = mantenimientoFeriadoService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feriado> find(@PathVariable("id") Integer id) {
        Feriado result = mantenimientoFeriadoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Feriado>> list(@RequestBody FeriadoFilter feriadoFilter){
        List<Feriado> result = mantenimientoFeriadoService.list(feriadoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody FeriadoBean feriadoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoFeriadoService.save(feriadoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}