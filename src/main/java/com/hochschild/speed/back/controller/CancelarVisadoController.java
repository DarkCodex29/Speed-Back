package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.utils.CancelarVisadoDTO;
import com.hochschild.speed.back.model.response.CancelarVisadoResponse;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.VisadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cancelarVisado")
public class CancelarVisadoController {
    Logger LOG = LoggerFactory.getLogger(CancelarVisadoController.class);
    private final VisadoService visadoService;

    private final CommonBusinessLogicService commonBusinessLogicService;

    private final JwtTokenUtil jwtTokenUtil;

    public CancelarVisadoController(VisadoService visadoService, CommonBusinessLogicService commonBusinessLogicService, JwtTokenUtil jwtTokenUtil) {
        this.visadoService = visadoService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    
    @GetMapping(value = "/verCancelarVisado/{idExpediente}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<CancelarVisadoResponse> verCancelarVisado(@PathVariable Integer idExpediente, HttpServletRequest request){
        Integer idUsuario = jwtTokenUtil.getIdUserFromRequest(request);
        return visadoService.getInfoCancelarVisado(idExpediente, idUsuario);
    }




    @RequestMapping(value="/cancelar",method=RequestMethod.PUT)
    public @ResponseBody ResponseEntity<ResponseModel> cancelarVisado(@RequestBody CancelarVisadoDTO cancelarVisadoDTO, HttpServletRequest request){
        try {
            Integer usuario= jwtTokenUtil.getIdUserFromRequest(request);
            if(usuario == null){
                throw new Exception("Usuario no logueado");
            }
            ResponseModel responseModel = visadoService.cancelarVisado(cancelarVisadoDTO.getIdExpediente(), usuario);
            return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());

        } catch (Exception ex){
            LOG.error(ex.getMessage(), ex);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("Usuario no Logeado");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
