package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.ObservarSolicitudBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.ObservarSolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/observarSolicitud")
public class ObservarSolicitudController {

    private final ObservarSolicitudService observarSolicitudService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ObservarSolicitudController(ObservarSolicitudService observarSolicitudService, JwtTokenUtil jwtTokenUtil) {
        this.observarSolicitudService = observarSolicitudService;
        this.jwtTokenUtil=jwtTokenUtil;

    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = {"/destinatario"},params = {"idExpediente"})
    public ResponseEntity<Map<String,Object>> obtenerResponsable(Integer idExpediente, HttpServletRequest request) {
        Map<String, Object> responseModel;
        try{
            responseModel = observarSolicitudService.obtenerDestinatario(idExpediente);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>( new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> observarSolicitud(@RequestBody ObservarSolicitudBean observarSolicitudBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        //String idPuesto = String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idPuesto"));

        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));

        ResponseModel responseModel = observarSolicitudService.observarSolicitud(observarSolicitudBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
