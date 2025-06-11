package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.utils.EntregaDocumentoDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.EntregaDocumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/entregaDocumento")
public class EntregaDocumentoController {

    private final EntregaDocumentoService entregaDocumentoService;
    private final JwtTokenUtil jwtTokenUtil;

    public EntregaDocumentoController(EntregaDocumentoService entregaDocumentoService, JwtTokenUtil jwtTokenUtil) {
        this.entregaDocumentoService = entregaDocumentoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value="/registrarEntrega",method= RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> registrarEntrega(@RequestBody EntregaDocumentoDTO entregaDocumentoDTO, HttpServletRequest request){
        Integer idUsuario= jwtTokenUtil.getIdUserFromRequest(request);
        ResponseModel result = entregaDocumentoService.registrarEntrega(entregaDocumentoDTO, idUsuario);
        return new ResponseEntity<>(result, result.getHttpSatus());
    }
}
