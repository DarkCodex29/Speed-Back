package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.GrupoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.GrupoUsuariosBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.HcGrupo;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.filter.mantenimiento.GrupoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoGrupoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/grupo")
public class HcGrupoController {
    private Logger LOGGER = LoggerFactory.getLogger(HcGrupoController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoGrupoService mantenimientoGrupoService;

    public HcGrupoController(JwtTokenUtil jwtTokenUtil,
                             MantenimientoGrupoService mantenimientoGrupoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoGrupoService = mantenimientoGrupoService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GrupoUsuariosBean> find(@PathVariable("id") Integer id) {
        GrupoUsuariosBean result = mantenimientoGrupoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcGrupo>> list(@RequestBody GrupoFilter grupoFilter){
        List<HcGrupo> result = mantenimientoGrupoService.list(grupoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoGrupos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Parametro>> listTipoGrupos(){
        List<Parametro> result = mantenimientoGrupoService.listTipoGrupos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/usuarios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> listUsuarios(){
        List<UsuariosBean> result = mantenimientoGrupoService.listUsuarios();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody GrupoBean grupoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoGrupoService.save(grupoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}