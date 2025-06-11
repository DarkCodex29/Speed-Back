package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.CampoBean;
import com.hochschild.speed.back.model.domain.speed.Campo;
import com.hochschild.speed.back.model.domain.speed.TipoCampo;
import com.hochschild.speed.back.model.filter.mantenimiento.CampoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoCampoService;
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
@RequestMapping("/campo")
public class CampoController {
    private Logger LOGGER = LoggerFactory.getLogger(CampoController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoCampoService mantenimientoCampoService;

    @Autowired
    public CampoController(JwtTokenUtil jwtTokenUtil, MantenimientoCampoService mantenimientoCampoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoCampoService = mantenimientoCampoService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Campo> find(@PathVariable("id") Integer id) {
        Campo result = mantenimientoCampoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Campo>> list(@RequestBody CampoFilter campoFilter){
        List<Campo> result = mantenimientoCampoService.list(campoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/fieldType", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoCampo>> fieldType(){
        List<TipoCampo> result = mantenimientoCampoService.fieldType();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/parameterType", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> parameterType(){
        List<String> result = mantenimientoCampoService.parameterType();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody CampoBean campoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoCampoService.save(campoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}