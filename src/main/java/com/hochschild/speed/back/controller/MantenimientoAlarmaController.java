package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.AlarmaBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.BandejaAlarmaBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoAlarmaService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mantenimientoAlarma")
public class MantenimientoAlarmaController {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoAlarmaController.class.getName());

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoAlarmaService mantenimientoAlarmaService;

    public MantenimientoAlarmaController(JwtTokenUtil jwtTokenUtil,
                                         MantenimientoAlarmaService mantenimientoAlarmaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoAlarmaService = mantenimientoAlarmaService;
    }

    @ResponseBody
    @GetMapping(value = "/bandejaAlarma/{idExpediente}")
    public ResponseEntity<BandejaAlarmaBean> bandejaAlarma(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        BandejaAlarmaBean result = mantenimientoAlarmaService.bandejaAlarma(idUsuario, idExpediente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registrarDocumentoLegalManual(@RequestBody AlarmaBean alarmaBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = mantenimientoAlarmaService.guardarAlarma(alarmaBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @ResponseBody
    @RequestMapping(value = "/eliminar/{idAlarma}", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registrarDocumentoLegalManual(@PathVariable("idAlarma") Integer idAlarma, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = mantenimientoAlarmaService.eliminarAlarma(idUsuario, idAlarma);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}