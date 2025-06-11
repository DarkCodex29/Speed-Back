package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.comunicarInteresados.ComunicarInteresadosBean;
import com.hochschild.speed.back.model.bean.registrarAdendaManual.InteresadosBean;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.ComunicarInteresadosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/comunicarInteresados")
public class ComunicarInteresadosController {
    private final JwtTokenUtil jwtTokenUtil;
    private final ComunicarInteresadosService comunicarInteresadosService;

    @Autowired
    public ComunicarInteresadosController(JwtTokenUtil jwtTokenUtil,
                                          ComunicarInteresadosService comunicarInteresadosService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.comunicarInteresadosService = comunicarInteresadosService;
    }
    @ResponseBody
    @RequestMapping(value = "/comunicarInteresadosModal/{idExpediente}", method = RequestMethod.GET)
    public ResponseEntity<List<InteresadoDTS>> comunicarInteresadosModal(@PathVariable Integer idExpediente, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        List<InteresadoDTS> responseModel = comunicarInteresadosService.getInteresadosIdExpediente(idExpediente, idUsuario);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "/buscarInteresados", method = RequestMethod.POST)
    public ResponseEntity<List<InteresadoDTS>> buscarInteresados(@RequestBody InteresadosBean interesadosBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        List<InteresadoDTS> responseModel = comunicarInteresadosService.buscarInteresados(interesadosBean.getTermino());
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @RequestMapping(value="/buscarInteresadosSeguridad", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<InteresadoDTS>> buscarInteresadosSeguridad(@RequestBody InteresadosBean interesadosBean, HttpServletRequest request){
        List<InteresadoDTS> responseModel = comunicarInteresadosService.buscarInteresadosSeguridad(interesadosBean.getTermino());
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/enviarComunicaciones", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registrarExpediente(@RequestBody ComunicarInteresadosBean comunicarInteresadosBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = comunicarInteresadosService.comunicarInteresados(comunicarInteresadosBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}