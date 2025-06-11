package com.hochschild.speed.back.controller.mobile;

import com.hochschild.speed.back.model.auth.User;
import com.hochschild.speed.back.model.auth.UserAuthExternal;
import com.hochschild.speed.back.model.bean.BandejaBean;
import com.hochschild.speed.back.model.bean.ObservarVisadoBean;
import com.hochschild.speed.back.model.bean.RevisarDocumentoBean;
import com.hochschild.speed.back.model.bean.TabModelBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DetalleExpedienteBean;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.HcUsuarioPorVisado;
import com.hochschild.speed.back.model.domain.speed.utils.ElementoListaVisadoDTO;
import com.hochschild.speed.back.model.filter.bandejaEntrada.BandejaEntradaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/speed-mobile")
@RequiredArgsConstructor
public class SpeedMobileController {
    private static final Logger LOGGER = Logger.getLogger(SpeedMobileController.class);
    private final VisadoService visadoService;
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarTrazaService revisarTrazaService;
    private final RevisarBuscarDocumentoService revisarBuscarDocumentoService;
    private final DescargarArchivoService descargarArchivoService;
    private final BandejaService bandejaService;
    private final RevisarDocumentoService revisarDocumentoService;

    @RequestMapping(value="/visado/listadoVisado",method= RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<ElementoListaVisadoDTO>> obtenerListadoVisado(HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String user = jwtTokenUtil.getUsernameFromToken(token);
        return visadoService.obtenerListadoVisado(user);
    }

    @ResponseBody
    @RequestMapping(value = "/bandeja/bandejaEntrada", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<BandejaBean>> bandejaEntrada(@RequestBody BandejaEntradaFilter bandejaEntradaFilter, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        bandejaEntradaFilter.setUsuario(idUsuario.toString());

        bandejaEntradaFilter.setEstado(String.valueOf(Constantes.ESTADO_HC_ENVIADO_VISADO));

        return bandejaService.obtenerBandejaEntrada(bandejaEntradaFilter);
    }

    @PostMapping("/auth/external")
    public ResponseEntity<User> loginExternal(@RequestBody UserAuthExternal userAuthExternal) throws Exception {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        User user = authService.loginExternalMobile(userAuthExternal.getKey());
        if (user != null) {
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(user, httpStatus);
    }

    @ResponseBody
    @GetMapping(value = "/revisarExpediente/obtenerDatosBasicosExpediente/{idTraza}")
    public ResponseEntity<TabModelBean> obtenerDatosBasicosExpediente(@PathVariable("idTraza") Integer idTraza, HttpServletRequest request) {

        TabModelBean result = revisarTrazaService.obtenerDatosBasicosExpediente(idTraza);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/revisarExpediente/revisarTraza/{idTraza}")
    public ResponseEntity<DetalleExpedienteBean> revisarExpedientePorTraza(@PathVariable("idTraza") Integer idTraza, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = obtenerDetalleExpedientePorTrazaMobile(revisarTrazaService.obtenerDetalleExpedientePorTraza(idTraza, idUsuario, idPerfil));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/revisarBuscarDocumento/{idExpediente}")
    public ResponseEntity<DetalleExpedienteBean> revisarBuscarDocumento(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);

        DetalleExpedienteBean result = obtenerDetalleExpedientePorTrazaMobile(revisarBuscarDocumentoService.obtenerDetalleContrato2(idExpediente, idUsuario, idPerfil));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/revisarDocumento/obtenerDocumento/{idDocumento}", method = RequestMethod.GET)
    public ResponseEntity<RevisarDocumentoBean> obtenerDocumento(@PathVariable("idDocumento") Integer idDocumento, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        //String idPuesto = String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idPuesto"));
        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
        Integer idPerfil = jwtTokenUtil.getIdPerfilFromToken(token);
        RevisarDocumentoBean responseModel = revisarDocumentoService.obtenerDocumento(idUsuario, idDocumento, true, idPerfil);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/descargarArchivo/{idArchivo}", method = RequestMethod.GET)
    public void descargar(@PathVariable("idArchivo") Integer idArchivo, final HttpServletResponse httpServletResponse) {
        descargarArchivoService.descargarArchivo(idArchivo,null, httpServletResponse);
    }


    @ResponseBody
    @RequestMapping(value = "/descargarArchivo/{idArchivo}/{version}", method = RequestMethod.GET)
    public void descargar(@PathVariable("idArchivo") Integer idArchivo,@PathVariable("version") String version, final HttpServletResponse httpServletResponse) {
        descargarArchivoService.descargarArchivo(idArchivo, version, httpServletResponse);
    }

    @ResponseBody
    @RequestMapping(value = "/descargarPlantilla", method = RequestMethod.POST)
    public void descargarByRuta(@RequestBody Map<String, String> archivo, final HttpServletResponse response) {
        descargarArchivoService.descargarPlantillaByRuta(archivo, response);
    }

    @ResponseBody
    @GetMapping(value = "/visado/verVisadores/{id}")
    public ResponseEntity<List<HcUsuarioPorVisado>> obtenerVisadores(@PathVariable("id") Integer idExpediente, HttpServletRequest request) {
        List<HcUsuarioPorVisado> result = visadoService.obtenerVisadores(idExpediente);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/visado/aprobar/{idExpediente}",method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> aprobarVisado(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        ResponseModel responseModel = visadoService.aprobarVisado(idExpediente, jwtTokenUtil.getIdUserFromRequest(request));
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = {"/visado/destinatario/{idExpediente}"})
    public ResponseEntity<Map<String,Object>> obtenerResponsable(@PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request) {
        Map<String, Object> responseModel;
        try{
            responseModel = visadoService.obtenerDestinatario(idExpediente);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>( new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @RequestMapping(value="/visado/observarVisado",method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<ResponseModel> observarVisado(@RequestBody ObservarVisadoBean observarVisadoBean, HttpServletRequest request) {
        try{
            ResponseModel responseModel = visadoService.observarVisado(observarVisadoBean, jwtTokenUtil.getIdUserFromRequest(request));
            return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
        } catch(Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private DetalleExpedienteBean obtenerDetalleExpedientePorTrazaMobile(DetalleExpedienteBean detalleExpedienteBean) {
        List<Boton> newBotones=new ArrayList<>();
        for(Boton boton: detalleExpedienteBean.getBotones()){
            if(boton.getUrl().equals("visado/verAprobar")||boton.getUrl().equals("visado/verObservar")){
                newBotones.add(boton);
            }
        }
        detalleExpedienteBean.setBotones(newBotones);
        return detalleExpedienteBean;
    }

}
