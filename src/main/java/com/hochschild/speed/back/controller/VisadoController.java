package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.ObservarVisadoBean;
import com.hochschild.speed.back.model.domain.speed.HcUsuarioPorVisado;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.VisadoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visado")
public class VisadoController {

    private static final Logger LOGGER = Logger.getLogger(VisadoController.class.getName());
    private final VisadoService visadoService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public VisadoController(VisadoService visadoService, JwtTokenUtil jwtTokenUtil) {
        this.visadoService = visadoService;
        this.jwtTokenUtil=jwtTokenUtil;
    }

    @ResponseBody
    @GetMapping(value = "/verVisadores/{id}")
    public ResponseEntity<List<HcUsuarioPorVisado>> obtenerVisadores(@PathVariable("id") Integer idExpediente, HttpServletRequest request) {
        List<HcUsuarioPorVisado> result = visadoService.obtenerVisadores(idExpediente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/aprobar/{idExpediente}",method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> aprobarVisado(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        ResponseModel responseModel = visadoService.aprobarVisado(idExpediente, jwtTokenUtil.getIdUserFromRequest(request));
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = {"/destinatario/{idExpediente}"})
    public ResponseEntity<Map<String,Object>> obtenerResponsable(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        Map<String, Object> responseModel;
        try{
            responseModel = visadoService.obtenerDestinatario(idExpediente);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>( new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @RequestMapping(value="/observarVisado",method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> observarVisado(@RequestBody ObservarVisadoBean observarVisadoBean, HttpServletRequest request) {
        try{
            ResponseModel responseModel = visadoService.observarVisado(observarVisadoBean, jwtTokenUtil.getIdUserFromRequest(request));
            return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
        } catch(Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
