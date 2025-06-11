package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.RevisarDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.utils.EstadoDocumentoDTO;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.RevisarDocumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/revisarDocumento")
public class RevisarDocumentoController {

    private final RevisarDocumentoService revisarDocumentoService;
    private final JwtTokenUtil jwtTokenUtil;
    public RevisarDocumentoController(RevisarDocumentoService revisarDocumentoService, JwtTokenUtil jwtTokenUtil) {
        this.revisarDocumentoService = revisarDocumentoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value = "/estadoDocumento", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, params = {"codigo"})
    @ResponseBody
    public ResponseEntity<List<EstadoDocumentoDTO>> listarPreguntas(@RequestParam("codigo") String codigo) {
        try {
            List<EstadoDocumentoDTO> estadoDocumentoDTOS = revisarDocumentoService.listarEstadosDocumento(codigo);
            return new ResponseEntity<>(estadoDocumentoDTOS, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/obtenerDocumento/{idDocumento}", method = RequestMethod.GET)
    public ResponseEntity<RevisarDocumentoBean> obtenerDocumento(@PathVariable("idDocumento") Integer idDocumento, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        //String idPuesto = String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idPuesto"));
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);
        RevisarDocumentoBean responseModel = revisarDocumentoService.obtenerDocumento(idUsuario, idDocumento, false, idPerfil);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}