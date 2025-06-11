package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoReeemplazoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ReemplazoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Reemplazo;
import com.hochschild.speed.back.model.filter.mantenimiento.ReemplazoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoReemplazoService;
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
@RequestMapping("/reemplazo")
public class ReemplazoController {
    private Logger LOGGER = LoggerFactory.getLogger(ReemplazoController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoReemplazoService mantenimientoReemplazoService;
    public ReemplazoController(JwtTokenUtil jwtTokenUtil,
                               MantenimientoReemplazoService mantenimientoReemplazoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoReemplazoService = mantenimientoReemplazoService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reemplazo> find(@PathVariable("id") Integer id) {
        Reemplazo result = mantenimientoReemplazoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reemplazo>> list(@RequestBody ReemplazoFilter reemplazoFilter){
        List<Reemplazo> result = mantenimientoReemplazoService.list(reemplazoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/reemplazados", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> listUsuarios(){
        List<UsuariosBean> result = mantenimientoReemplazoService.listUsuarios();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/reemplazantes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> listJefes(@PathVariable("id") Integer id) {
        List<UsuariosBean> result = mantenimientoReemplazoService.listJefes(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/procesos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcesoReeemplazoBean>> listProcesos(){
        List<ProcesoReeemplazoBean> result = mantenimientoReemplazoService.listProcesos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/procesosDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcesoReeemplazoBean>> procesosDisponibles(@PathVariable("id") Integer id){
        List<ProcesoReeemplazoBean> result = mantenimientoReemplazoService.listProcesosDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody ReemplazoBean reemplazoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoReemplazoService.save(reemplazoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}