package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.CompraTransaccionBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.CompraTransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/compraTransaccion")
public class CompraTransaccionController {
    private final JwtTokenUtil jwtTokenUtil;
    private final CompraTransaccionService compraTransaccionService;

    public CompraTransaccionController(JwtTokenUtil jwtTokenUtil, CompraTransaccionService compraTransaccionService){
        this.jwtTokenUtil = jwtTokenUtil;
        this.compraTransaccionService = compraTransaccionService;
    }

    @RequestMapping(value = "/buscar", method = RequestMethod.GET)
    public ResponseEntity<List<CompraTransaccionBean>> listarRegistros(@RequestParam(required = false) String descripcion, HttpServletRequest request) {
        descripcion = descripcion != null ? descripcion : "";
        List<CompraTransaccionBean> response = compraTransaccionService.listarByEstado(descripcion);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registrar(@Valid @RequestBody CompraTransaccionBean compraTransaccionBean, HttpServletRequest request) {
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        ResponseModel response = compraTransaccionService.guardar(compraTransaccionBean, idUsuario);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseModel> registrar(@Valid @PathVariable Integer id, HttpServletRequest request) {
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        ResponseModel response = compraTransaccionService.eliminar(id, idUsuario);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
