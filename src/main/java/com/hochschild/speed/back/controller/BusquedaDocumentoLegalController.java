package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocDashboardBean;
import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocumentoBean;
import com.hochschild.speed.back.model.filter.BuscarDocumentoLegalFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.BusquedaDocumentoLegalService;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/buscarDocumentosHC")
public class BusquedaDocumentoLegalController {

    private static final Logger LOGGER = Logger.getLogger(BusquedaDocumentoLegalController.class.getName());
    private final BusquedaDocumentoLegalService busquedaDocumentoLegalService;
    private final BusquedaDocumentoLegalService busquedaService;
    private final JwtTokenUtil jwtTokenUtil;

    public BusquedaDocumentoLegalController(BusquedaDocumentoLegalService busquedaDocumentoLegalService, BusquedaDocumentoLegalService busquedaService, JwtTokenUtil jwtTokenUtil) {
        this.busquedaDocumentoLegalService = busquedaDocumentoLegalService;
        this.busquedaService = busquedaService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/buscar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilaBusquedaDocumentoBean>> misPendientes(@RequestBody BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, HttpServletRequest request) {

        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        buscarDocumentoLegalFilter.setIdUsuario(idUsuario);

        return busquedaDocumentoLegalService.buscarDocumentosLegales(buscarDocumentoLegalFilter, false);
    }
    @ResponseBody
    @RequestMapping(value = "/buscarExcel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilaBusquedaDocumentoBean>> misPendientesExcel(@RequestBody BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, HttpServletRequest request) {

        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        buscarDocumentoLegalFilter.setIdUsuario(idUsuario);

        return busquedaDocumentoLegalService.buscarDocumentosLegales(buscarDocumentoLegalFilter, true);
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/exportarExcel", method = RequestMethod.GET)
    public void excelDocumentosLegales(@RequestBody BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, HttpSession session, HttpServletResponse response) {

        Workbook objetoExcel = busquedaService.exportarBusquedaDL(buscarDocumentoLegalFilter);

        if (objetoExcel != null) {
            try {
                response.reset();
                response.setContentType("application/excel");
                response.setHeader("Content-Disposition", "attachment; filename=Export.xls");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-control", "no-cache");
                response.setHeader("Cache-Control", "no-store");
                objetoExcel.write(response.getOutputStream());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.info("Error al exportar a Excel");
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/buscarDashboard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilaBusquedaDocDashboardBean>> misPendientesDashboard(@RequestBody BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, HttpServletRequest request) {

        Integer idUsuario = jwtTokenUtil.getIdUserFromToken(jwtTokenUtil.getTokenFromRequest(request));
        buscarDocumentoLegalFilter.setIdUsuario(idUsuario);

        return busquedaDocumentoLegalService.buscarDocumentosLegalesDashboard(buscarDocumentoLegalFilter);
    }
}