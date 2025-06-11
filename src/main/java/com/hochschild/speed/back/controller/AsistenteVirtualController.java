package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.RegistrarPreguntaAsistenteVirtualBean;
import com.hochschild.speed.back.model.bean.RegistrarUsabilidadBean;
import com.hochschild.speed.back.model.domain.speed.PreguntasAsistenteVirtual;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.PreguntaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("asistenteVirtual")
public class AsistenteVirtualController {
    Logger LOG = LoggerFactory.getLogger(AsistenteVirtualController.class);
    private final PreguntaService preguntaService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AsistenteVirtualController(PreguntaService preguntaService, JwtTokenUtil jwtTokenUtil) {
        this.preguntaService = preguntaService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PreguntasAsistenteVirtual>> listQuestions() {
        try {
            return new ResponseEntity<>(preguntaService.listarPreguntas(), HttpStatus.OK);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/registrarPregunta", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseModel> registrarPregunta(HttpServletRequest request, @RequestBody RegistrarPreguntaAsistenteVirtualBean preguntaAsistenteVirtualBean) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        return preguntaService.registrarPregunta(null, preguntaAsistenteVirtualBean.getPregunta(), idUsuario);
    }

    @RequestMapping(value = "/registrarUsabilidad", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseModel> registrarUsabilidad(HttpServletRequest request, @RequestBody RegistrarUsabilidadBean registrarUsabilidadBean) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        return preguntaService.registrarUsabilidad(null, registrarUsabilidadBean.getIdOpcionAsistente(), registrarUsabilidadBean.getEsOpcionAsistente(), idUsuario);
    }

}
