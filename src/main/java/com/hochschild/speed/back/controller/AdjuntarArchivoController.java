package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.AdjuntarArchivoBean;
import com.hochschild.speed.back.model.bean.VerificarArchivoBean;
import com.hochschild.speed.back.model.bean.adjuntarDocumento.VerificarArchivoExpBean;
import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.ArchivoRepository;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.AdjuntarArchivoService;
import com.hochschild.speed.back.service.AdjuntarDocumentoService;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/adjuntarArchivo")
public class AdjuntarArchivoController {

    private final AdjuntarArchivoService adjuntarArchivoService;
    private final AdjuntarDocumentoService adjuntarDocumentoService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AdjuntarArchivoController(AdjuntarArchivoService adjuntarArchivoService,
                                     AdjuntarDocumentoService adjuntarDocumentoService,
                                     JwtTokenUtil jwtTokenUtil) {
        this.adjuntarArchivoService = adjuntarArchivoService;
        this.adjuntarDocumentoService = adjuntarDocumentoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/verificarArchivoEnExpediente/{idExpediente}", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> verificarArchivoEnExpediente(@PathVariable Integer idExpediente, @RequestBody VerificarArchivoExpBean verificarArchivoExpBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = adjuntarArchivoService.verificarExistenciaArchivoEnExpediente(idExpediente, verificarArchivoExpBean.getNombreArchivoDisco(), idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @ResponseBody
    @RequestMapping(value = "/verificarArchivo", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> verificarArchivo(@RequestBody VerificarArchivoBean verificarArchivoBean, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        if(verificarArchivoBean.getNombreArchivoDisco() != null && verificarArchivoBean.getNombreArchivoDisco().contains("&")){
            String nombreArchivoDisco = StringEscapeUtils.unescapeHtml(verificarArchivoBean.getNombreArchivoDisco());
            verificarArchivoBean.setNombreArchivoDisco(nombreArchivoDisco);
        }
        try{
            responseModel = adjuntarArchivoService.verificarExistenciaArchivo(verificarArchivoBean.getIdDocumento(), verificarArchivoBean.getNombreArchivoDisco());

        } catch (Exception ex){
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> adjuntarArchivo(@RequestBody AdjuntarArchivoBean adjuntarArchivoBean, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = new ResponseModel();
        if(adjuntarArchivoBean.getNombreArchivoDisco() != null && adjuntarArchivoBean.getNombreArchivoDisco().contains("&")){
          String  nombreArchivoDisco = StringEscapeUtils.unescapeHtml(adjuntarArchivoBean.getNombreArchivoDisco());
            adjuntarArchivoBean.setNombreArchivoDisco(nombreArchivoDisco);
        }

        try{
            Archivo archivo=adjuntarArchivoService.subirArchivo(adjuntarArchivoService.obtenerDocumentoPorId(adjuntarArchivoBean.getIdDocumento()),adjuntarArchivoBean.getNombreArchivoDisco(),null,idUsuario,null);
            responseModel.setMessage(archivo.getRutaAlfresco());
            responseModel.setId(archivo.getId());
            responseModel.setHttpSatus(HttpStatus.OK);
        }

        catch(ExcepcionAlfresco ex1){

            responseModel.setMessage(null);
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @RequestMapping(value="/subirArchivoAlfresco",method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> subirArchivoAlfresco(@RequestBody AdjuntarArchivoBean adjuntarArchivoBean, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = new ResponseModel();
        try{
            Archivo archivo=adjuntarArchivoService.subirArchivoAlfrescoSinGuardar(adjuntarArchivoBean.getIdDocumento(), adjuntarArchivoBean.getIdArchivo(), idUsuario);
            responseModel.setMessage(archivo.getRutaAlfresco());
            responseModel.setId(archivo.getId());
            responseModel.setHttpSatus(HttpStatus.OK);
        }
        catch(ExcepcionAlfresco ex1){
            responseModel.setMessage(null);
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());

    }
    @ResponseBody
    @RequestMapping(value = "/eliminarArchivo", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> eliminarArchivo(@RequestBody Integer[] idArchivos, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        ResponseModel responseModel = adjuntarDocumentoService.eliminarArchivoPorId(idArchivos, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @ResponseBody
    @RequestMapping(value = "/obtenerUrlAlfresco/{idArchivo}", method = RequestMethod.GET)
    public ResponseEntity<String> obtenerUrlAlfresco(@PathVariable Integer idArchivo, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        String responseModel = adjuntarArchivoService.obtenerUrlAlfresco(idArchivo);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "/getDownloadUrl/{idArchivo}", method = RequestMethod.GET)
    public ResponseEntity<String> getDownloadUrl(@PathVariable Integer idArchivo, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        String responseModel = adjuntarArchivoService.getDownloadUrl(idArchivo);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
    
    @ResponseBody
    @RequestMapping(value = "/obtenerIdAlfresco/{idArchivo}", method = RequestMethod.GET)
    public ResponseEntity<String> obtenerIdAlfresco(@PathVariable Integer idArchivo, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = new ResponseModel();
        String documento =adjuntarArchivoService.obtenerIdAlfresco(idArchivo);
        return new ResponseEntity<>(documento, HttpStatus.OK);
    }
}
