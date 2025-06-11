package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.RevisarExpedienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/revisarDocumentoLegal")
public class RevisarDocumentoLegalController {
    private static Logger logger = LoggerFactory.getLogger(RevisarDocumentoLegalController.class);
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final RevisarExpedienteService revisarExpedienteService;
    private final JwtTokenUtil jwtTokenUtil;

    public RevisarDocumentoLegalController(CommonBusinessLogicService commonBusinessLogicService, RevisarExpedienteService revisarExpedienteService, JwtTokenUtil jwtTokenUtil) {
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.revisarExpedienteService = revisarExpedienteService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @GetMapping(value = "/obtenerContratoAdenda/{idExpediente}")
    public ResponseEntity<Map<String, Object>> obtenerContratoPorAdenda(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);
        try{
            HcDocumentoLegal adenda = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
            HcDocumentoLegal contrato = commonBusinessLogicService.buscarContratoPorAdenda(adenda.getId());
            Traza traza = revisarExpedienteService.obtenerUltimaTraza(contrato.getExpediente().getId(), idUsuario);
            Map<String, Object> result = new HashMap<>();
            result.put("id",traza.getId());
            result.put("code", contrato.getNumero());
            result.put("idExpediente", contrato.getExpediente().getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
