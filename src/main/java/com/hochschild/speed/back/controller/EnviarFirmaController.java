package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.EnviarFirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/enviarFirma")
public class EnviarFirmaController {
    private final EnviarFirmaService enviarFirmaService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public EnviarFirmaController(EnviarFirmaService enviarFirmaService, JwtTokenUtil jwtTokenUtil) {
        this.enviarFirmaService = enviarFirmaService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value = "/{idExpediente}", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> enviar(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        ResponseModel responseModel = enviarFirmaService.enviarFirma(idExpediente, jwtTokenUtil.getIdUserFromRequest(request));
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
