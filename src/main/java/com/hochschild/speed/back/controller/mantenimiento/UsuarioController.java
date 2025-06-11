package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.JefeBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuarioBean;
import com.hochschild.speed.back.model.bean.mantenimiento.UsuariosBean;
import com.hochschild.speed.back.model.domain.speed.Area;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.filter.mantenimiento.UsuarioFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
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
 * @since 24/11/2023
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoUsuarioService mantenimientoUsuarioService;
    public UsuarioController(JwtTokenUtil jwtTokenUtil, MantenimientoUsuarioService mantenimientoUsuarioService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoUsuarioService = mantenimientoUsuarioService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> find(@PathVariable("id") Integer id) {
        Usuario result = mantenimientoUsuarioService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosBean>> list(@RequestBody UsuarioFilter usuarioFilter){
        List<UsuariosBean> result = mantenimientoUsuarioService.list(usuarioFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/jefes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JefeBean>> listJefes(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        List<JefeBean> result = mantenimientoUsuarioService.listJefes(idUsuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/roles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> listRoles(){
        List<Rol> result = mantenimientoUsuarioService.listRoles();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/perfiles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Perfil>> listPerfiles(){
        List<Perfil> result = mantenimientoUsuarioService.listPerfiles();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/areas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Area>> listAreas(){
        List<Area> result = mantenimientoUsuarioService.listAreas();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/rolesDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> rolesDisponibles(@PathVariable("id") Integer id){
        List<Rol> result = mantenimientoUsuarioService.listRolesUsuarioDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/perfilesDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Perfil>> perfilesDisponibles(@PathVariable("id") Integer id){
        List<Perfil> result = mantenimientoUsuarioService.listPerfilesDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody UsuarioBean usuarioBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoUsuarioService.save(usuarioBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}