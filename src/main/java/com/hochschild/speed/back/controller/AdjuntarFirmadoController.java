package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.GuardarArchivoFirmadoBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.AdjuntarFirmadoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/adjuntarFirmado")
public class AdjuntarFirmadoController {

    private final AdjuntarFirmadoService adjuntarFirmadoService;

    private final JwtTokenUtil jwtTokenUtil;

    public AdjuntarFirmadoController(AdjuntarFirmadoService adjuntarFirmadoService, JwtTokenUtil jwtTokenUtil) {
        this.adjuntarFirmadoService = adjuntarFirmadoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @ResponseBody
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseModelFile> uploadFiles(@RequestPart MultipartFile archivoSubir, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = jwtTokenUtil.getIdUserFromRequest(request);
        ResponseModelFile responseModel = adjuntarFirmadoService.uploadFile(archivoSubir, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @RequestMapping(value = "/guardarFirmado", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ResponseModel> guardarFirmado(@RequestBody GuardarArchivoFirmadoBean guardarArchivoFirmadoBean
            , HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = jwtTokenUtil.getIdUserFromRequest(request);
        ResponseModel responseModel = adjuntarFirmadoService.guardarFirmado(guardarArchivoFirmadoBean.getIdDocumento(), guardarArchivoFirmadoBean.getArchivo(),idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
