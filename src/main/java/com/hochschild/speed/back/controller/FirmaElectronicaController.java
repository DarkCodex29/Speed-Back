package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.firmaElectronica.EnvioFirmaBean;
import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.FirmaElectronicaService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firmaElectronica")
public class FirmaElectronicaController {
    private final FirmaElectronicaService firmaElectronicaService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public FirmaElectronicaController(
            FirmaElectronicaService firmaElectronicaService,
            JwtTokenUtil jwtTokenUtil) {
        this.firmaElectronicaService = firmaElectronicaService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/enviar")
    public @ResponseBody
    ResponseEntity<ResponseModel> enviarFirma(@RequestBody EnvioFirmaBean envioFirmaBean, HttpServletRequest request) {
        return AppUtil.convertToResponseEntity(firmaElectronicaService.createContrato(
                envioFirmaBean.getIdExpediente()
                , jwtTokenUtil.getIdUserFromRequest(request)
                , envioFirmaBean.getIdioma()
                , envioFirmaBean.getIdRepresentante()
                , envioFirmaBean.getTipoFirma(), true));
    }

    @PostMapping(value = "/reenviarNotificacion/{idExpediente}")
    public @ResponseBody
    ResponseEntity<ResponseModel> reenviarNotificacion(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        return AppUtil.convertToResponseEntity(firmaElectronicaService.reenviarNotificacion(idExpediente, request));
    }

    @PostMapping(value = "/reenviar")
    public @ResponseBody
    ResponseEntity<ResponseModel> reenviarFirma(@RequestBody EnvioFirmaBean envioFirmaBean, HttpServletRequest request) {
       return AppUtil.convertToResponseEntity(firmaElectronicaService.resendContrato(envioFirmaBean.getIdExpediente()
                , jwtTokenUtil.getIdUserFromRequest(request)
                , envioFirmaBean.getIdioma()
                , envioFirmaBean.getIdRepresentante()
                , envioFirmaBean.getTipoFirma()));
    }

    @GetMapping(value = "/detalleContrato/{idExpediente}")
    public @ResponseBody
    ResponseEntity<Map<String, Object>> detalleContrato(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        Map<String, Object> result = firmaElectronicaService.getContratoDetail(idExpediente);
        if (result.get("resultado").equals("error")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/eliminar/{idExpediente}")
    public @ResponseBody
    ResponseEntity<ResponseModel> eliminarContrato(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        return AppUtil.convertToResponseEntity(firmaElectronicaService.deleteFirmaElectronica(idExpediente,jwtTokenUtil.getIdUserFromRequest(request),true, true));
    }
    @GetMapping(value = "/representantesCompania")
    public ResponseEntity<List<Usuario>> getRepresentantesCompania(){
        return new ResponseEntity<>(firmaElectronicaService.getListaRepresentantes(Constantes.ESTADO_REPRESENTANTE_COMPANHIA_HABILITADO), HttpStatus.OK);
    }
    @GetMapping(value = "/getTiposFirma")
    public ResponseEntity<List<Parametro>> getTiposFirma(){
        return new ResponseEntity<>(firmaElectronicaService.getParametrosPorTipo(Constantes.TIPO_PARAMETRO_FIRMA_ELECTRONICA), HttpStatus.OK);
    }

    @GetMapping(value = "/getTiposIdiomas")
    public ResponseEntity<List<Parametro>> getTiposIdiomas(){
        return new ResponseEntity<>(firmaElectronicaService.getParametrosPorTipo(Constantes.TIPO_PARAMETRO_IDIOMA_FIRMA_ELECTRONICA), HttpStatus.OK);
    }
}
