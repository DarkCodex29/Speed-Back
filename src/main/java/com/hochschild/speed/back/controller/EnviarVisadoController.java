package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.EnviarVisadoBean;
import com.hochschild.speed.back.model.bean.registrarAdendaManual.InteresadosBean;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.EnviarVisadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/enviarVisado")
public class EnviarVisadoController {

    private final EnviarVisadoService enviarVisadoService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public EnviarVisadoController(EnviarVisadoService enviarVisadoService,
                                      JwtTokenUtil jwtTokenUtil) {
        this.enviarVisadoService = enviarVisadoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value = "/enviar", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> enviarVisado(@RequestBody EnviarVisadoBean enviarVisadoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = enviarVisadoService.enviarVisado(enviarVisadoBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @RequestMapping(value="/buscarVisadores",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<InteresadoDTS>> buscarVisadores(@RequestBody InteresadosBean interesadosBean){
        List<InteresadoDTS> responseModel = enviarVisadoService.buscarVisadores(interesadosBean.getTermino());
        return new ResponseEntity<>(responseModel,  HttpStatus.OK);
    }

    @RequestMapping(value = "/validarArchivoPdf/{idExpediente}", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> validarArchivoPdf(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = enviarVisadoService.validarArchivoPdf(idExpediente);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
