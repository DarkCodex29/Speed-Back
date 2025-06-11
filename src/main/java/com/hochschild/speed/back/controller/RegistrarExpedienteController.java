package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.BorrarArchivoBean;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.ExpedienteService;
import com.hochschild.speed.back.service.ProcesoService;
import com.hochschild.speed.back.service.RegistrarExpedienteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CAPSULA DIGITAL
 * @since 12/06/2022
 */

@RestController
@RequestMapping("/registrarExpediente")
public class RegistrarExpedienteController {
    private static final Logger LOGGER = Logger.getLogger(RegistrarExpedienteController.class.getName());
    private final RegistrarExpedienteService registrarExpedienteService;
    private final ExpedienteService expedienteService;
    private final ProcesoService procesoService;
    private final CommonBusinessLogicService commonBusinessLogicService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RegistrarExpedienteController(RegistrarExpedienteService registrarExpedienteService,
                                         ExpedienteService expedienteService,
                                         ProcesoService procesoService,
                                         CommonBusinessLogicService commonBusinessLogicService,
                                         JwtTokenUtil jwtTokenUtil) {
        this.registrarExpedienteService = registrarExpedienteService;
        this.expedienteService = expedienteService;
        this.procesoService = procesoService;
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseModelFile> uploadFiles(@RequestPart MultipartFile archivoSubir, HttpServletRequest request) {
        LOGGER.info(archivoSubir.getSize());
        String token = request.getHeader("Authorization").substring(7);

        Integer idUsuario = Integer.valueOf(String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idUsuario")));

        ResponseModelFile responseModel = registrarExpedienteService.subirArchivo(archivoSubir, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @RequestMapping(value = "/deleteFiles", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<ResponseModel> eliminarImagen(@RequestBody BorrarArchivoBean borrarArchivoBean) {
        ResponseModel responseModel = registrarExpedienteService.eliminarArchivo(borrarArchivoBean.getFile());
        return  new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

}
