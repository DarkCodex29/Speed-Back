package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.PenalidadBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ReiteranciasBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoValorBean;
import com.hochschild.speed.back.model.domain.speed.HcPenalidad;
import com.hochschild.speed.back.model.filter.mantenimiento.PenalidadFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoPenalidadService;
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
@RequestMapping("/penalidad")
public class HcPenalidadController {
    private Logger LOGGER = LoggerFactory.getLogger(HcPenalidadController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoPenalidadService mantenimientoPenalidadService;

    public HcPenalidadController(JwtTokenUtil jwtTokenUtil, MantenimientoPenalidadService mantenimientoPenalidadService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoPenalidadService = mantenimientoPenalidadService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HcPenalidad> find(@PathVariable("id") Integer id) {
        HcPenalidad result = mantenimientoPenalidadService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcPenalidad>> list(@RequestBody PenalidadFilter penalidadFilter){
        List<HcPenalidad> result = mantenimientoPenalidadService.list(penalidadFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/reiterancias", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReiteranciasBean>> listReiterancias(){
        List<ReiteranciasBean> result = mantenimientoPenalidadService.listReiterancias();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoValores", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoValorBean>> listTipoValores(){
        List<TipoValorBean> result = mantenimientoPenalidadService.listTipoValores();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody PenalidadBean penalidadBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoPenalidadService.save(penalidadBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}