package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.GenerarHcDocumentoFromPlantillaService;
import com.hochschild.speed.back.util.StringUtils;
import com.hochschild.speed.back.util.exception.ExcepcionNoPlantilla;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/generarPlantilla")
public class GenerarPlantillaController {
    Logger LOGGER = Logger.getLogger(GenerarPlantillaController.class);

    private final GenerarHcDocumentoFromPlantillaService generarHcDocumentoFromPlantillaService;

    private final CommonBusinessLogicService commonBusinessLogicService;


    public GenerarPlantillaController(GenerarHcDocumentoFromPlantillaService generarHcDocumentoFromPlantillaService, CommonBusinessLogicService commonBusinessLogicService) {
        this.generarHcDocumentoFromPlantillaService = generarHcDocumentoFromPlantillaService;
        this.commonBusinessLogicService = commonBusinessLogicService;
    }
    @RequestMapping(value = "/findLstHcTipoContratoCodigoAdenda", method = RequestMethod.GET)
    @ResponseBody
    public List<HcTipoContrato> findLstHcTipoContratoCodigoAdenda() {
        return generarHcDocumentoFromPlantillaService.findLstHcTipoContratoCodigoAdenda();
    }

    @RequestMapping(value = "/validar", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> validarPlantilla(@RequestParam(name = "idDocumento") Integer idDocumento, @RequestParam(value = "idHcTipoContrato", required = false) Integer idHcTipoContrato) throws Exception {
        Boolean result;
        Documento documento = commonBusinessLogicService.obtenerDocumento(idDocumento);
        HcDocumentoLegal hcDocumentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegal(documento.getExpediente().getDocumentoLegal().getId());

        LOGGER.debug("idDocumento "+  idDocumento +" idHcTipoContrato "+ idHcTipoContrato);
        result = generarHcDocumentoFromPlantillaService.validarPlantilla(hcDocumentoLegal, idHcTipoContrato);

        if (!result) {
            throw new ExcepcionNoPlantilla("No hay plantilla");
        }
        ResponseModel responseModel = new ResponseModel();
        responseModel.setMessage("OK");
        responseModel.setHttpSatus(HttpStatus.OK);
         return  new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/plantilla", method = RequestMethod.GET)
    public void plantilla(@RequestParam("idDocumento") Integer idDocumento, @RequestParam(value = "idHcTipoContrato", required = false) Integer idHcTipoContrato, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Documento documento = commonBusinessLogicService.obtenerDocumento(idDocumento);
        HcDocumentoLegal hcDocumentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegal(documento.getExpediente().getDocumentoLegal().getId());
        Path file;
        String outputFilePathName;

        //log.debug("idDocumento [{}] idHcTipoContrato [{}]", idDocumento, idHcTipoContrato);
        outputFilePathName = generarHcDocumentoFromPlantillaService.generarHcDocumentoFromPlantilla(hcDocumentoLegal, idHcTipoContrato);

        if (StringUtils.isEmpty(outputFilePathName)) {
            throw new ExcepcionNoPlantilla("No hay plantilla");
        }

        //log.debug("documento generado [{}]", outputFilePathName);
        file = Paths.get(outputFilePathName);

        if (Files.exists(file)) {
            response.setContentType("application/msword");
            response.addHeader("Content-Disposition", "attachment; filename=" + outputFilePathName.substring(outputFilePathName.lastIndexOf("/") + 1));
            Files.copy(file, response.getOutputStream());
            response.getOutputStream().flush();
            return;
        }

        throw new RuntimeException("Ocurrio un error en el sistema");
    }


    @ExceptionHandler(ExcepcionNoPlantilla.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseModel> handleExcepcionNoPlantilla(ExcepcionNoPlantilla e) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setMessage("No existe plantilla");
        responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return "Ocurrio un error en el sistema";
    }

}
