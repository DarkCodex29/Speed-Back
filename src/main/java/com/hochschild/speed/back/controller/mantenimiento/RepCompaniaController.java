package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.RepresentanteCompBean;
import com.hochschild.speed.back.model.bean.mantenimiento.RepresentantesBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.RepresentanteComp;
import com.hochschild.speed.back.model.filter.mantenimiento.RepCompaniaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoRepCompaniaService;
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
@RequestMapping("/representanteCompania")
public class RepCompaniaController {
    private Logger LOGGER = LoggerFactory.getLogger(RepCompaniaController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoRepCompaniaService mantenimientoRepCompaniaService;

    public RepCompaniaController(JwtTokenUtil jwtTokenUtil,
                                 MantenimientoRepCompaniaService mantenimientoRepCompaniaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoRepCompaniaService = mantenimientoRepCompaniaService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentanteComp> find(@PathVariable("id") Integer id) {
        RepresentanteComp result = mantenimientoRepCompaniaService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RepresentantesBean>> list(@RequestBody RepCompaniaFilter repCompaniaFilter){
        List<RepresentantesBean> result = mantenimientoRepCompaniaService.list(repCompaniaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> listUsuarios(){
        List<UsuariosBean> result = mantenimientoRepCompaniaService.listUsuarios();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody RepresentanteCompBean representanteCompBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoRepCompaniaService.save(representanteCompBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}