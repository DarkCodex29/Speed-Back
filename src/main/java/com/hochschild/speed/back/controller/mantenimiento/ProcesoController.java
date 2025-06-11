package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.DataProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import com.hochschild.speed.back.model.filter.mantenimiento.ProcesoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoProcesoService;
import com.hochschild.speed.back.service.MantenimientoUsuarioService;
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
@RequestMapping("/proceso")
public class ProcesoController {
    private Logger LOGGER = LoggerFactory.getLogger(ProcesoController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoProcesoService mantenimientoProcesoService;
    private final MantenimientoUsuarioService mantenimientoUsuarioService;
    public ProcesoController(JwtTokenUtil jwtTokenUtil,
                             MantenimientoProcesoService mantenimientoProcesoService,
                             MantenimientoUsuarioService mantenimientoUsuarioService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoProcesoService = mantenimientoProcesoService;
        this.mantenimientoUsuarioService = mantenimientoUsuarioService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataProcesoBean> find(@PathVariable("id") Integer id) {
        DataProcesoBean result = mantenimientoProcesoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataProcesoBean>> list(@RequestBody ProcesoFilter procesoFilter){
        List<DataProcesoBean> result = mantenimientoProcesoService.list(procesoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoProcesos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoProceso>> listTipoProcesos(){
        List<TipoProceso> result = mantenimientoProcesoService.listTipoProcesos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/confidencialidad", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Parametro>> listConfidencialidad(){
        List<Parametro> result = mantenimientoProcesoService.listConfidencialidad();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/usuariosParticipante", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> listUsuariosParticipante(){
        List<UsuariosBean> result = mantenimientoProcesoService.listUsuarios();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/usuariosParticipanteDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> listUsuariosParticipanteDisponibles(@PathVariable("id") Integer id){
        List<UsuariosBean> result = mantenimientoProcesoService.listUsuariosDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rolesParticipante", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> listRolesParticipante(){
        List<Rol> result = mantenimientoUsuarioService.listRoles();;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rolesParticipanteDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> listRolesParticipanteDisponibles(@PathVariable("id") Integer id){
        List<Rol> result = mantenimientoUsuarioService.listRolesParticipanteDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rolesProceso", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> listRolesProceso(){
        List<Rol> result = mantenimientoUsuarioService.listRoles();;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rolesProcesoDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> listRolesProcesoDisponibles(@PathVariable("id") Integer id){
        List<Rol> result = mantenimientoUsuarioService.listRolesProcesoDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tiposDocumento", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoDocumento>> listTiposDocumento(){
        List<TipoDocumento> result = mantenimientoProcesoService.listTipoDocumento();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tiposDocumentoDisponible/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoDocumento>> lisTiposDocumentoDisponible(@PathVariable("id") Integer id){
        List<TipoDocumento> result = mantenimientoProcesoService.listTipoDocumentoDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody ProcesoBean procesoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoProcesoService.save(procesoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}