package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.EstadoBean;
import com.hochschild.speed.back.model.bean.seguimientoSolicitudes.ListadoSolicitudesBean;
import com.hochschild.speed.back.model.domain.speed.utils.FilaSeguimientoSolicitudDTS;
import com.hochschild.speed.back.model.filter.SeguimientoSolicitudFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.BusquedaDocumentoLegalService;
import com.hochschild.speed.back.service.EstadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/seguimientoSolicitud")
public class SeguimientoSolicitudController {
    private final JwtTokenUtil jwtTokenUtil;
    private final EstadoService estadoService;
    private final BusquedaDocumentoLegalService busquedaDocumentoLegalService;

    public SeguimientoSolicitudController(JwtTokenUtil jwtTokenUtil,
                                          EstadoService estadoService,
                                          BusquedaDocumentoLegalService busquedaDocumentoLegalService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.estadoService = estadoService;
        this.busquedaDocumentoLegalService = busquedaDocumentoLegalService;
    }

    @ResponseBody
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RequestMapping(value = "/obtenerEstados", method = RequestMethod.GET)
    public ResponseEntity<List<EstadoBean>> obtenerEstados() {

        List<EstadoBean> result = estadoService.obtenerEstadosSeguimientoSolicitud();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/buscar")
    public ResponseEntity<List<FilaSeguimientoSolicitudDTS>> buscar(@RequestBody SeguimientoSolicitudFilter filter) {

        return new ResponseEntity<>(busquedaDocumentoLegalService.buscarSeguimientoSolicitud(filter), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/guardarSolicitudes", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarDocumentoLegal(@RequestBody ListadoSolicitudesBean listadoSolicitudesBean) {

        ResponseModel responseModel = busquedaDocumentoLegalService.guardarSolicitudesPorDocumentoLegal(listadoSolicitudesBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
