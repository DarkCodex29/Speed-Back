package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.reportefilter.*;
import com.hochschild.speed.back.model.reporte.*;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.*;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/reporte")
public class ReporteController {
    private static Logger LOGGER = LoggerFactory.getLogger(ReporteController.class);
    final MantenimientoProcesoService mantenimientoProcesoService;
    final MantenimientoNumeracionService mantenimientoNumeracionService;

    final MantenimientoAreaService mantenimientoAreaService;
    final RegistrarExpedienteService registrarExpedienteService;

    final HcUbicacionService hcUbicacionService;
    final MantenimientoGrupoService mantenimientoHcGrupoService;
    final ExpedienteService expedienteService;

    final ReportService reportService;

    final ServicioDTSService servicioDTSService;

    final ReporteArrendamientoService reporteArrendamientoService;

    final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ReporteController(JwtTokenUtil jwtTokenUtil, ServicioDTSService servicioDTSService, ExpedienteService expedienteService, MantenimientoNumeracionService mantenimientoNumeracionService, MantenimientoProcesoService mantenimientoProcesoService, RegistrarExpedienteService registrarExpedienteService, MantenimientoAreaService mantenimientoAreaService, HcUbicacionService hcUbicacionService, MantenimientoGrupoService mantenimientoGrupoService, ReportService reportService, ReporteArrendamientoService reporteArrendamientoService) {
        this.mantenimientoNumeracionService = mantenimientoNumeracionService;
        this.mantenimientoProcesoService = mantenimientoProcesoService;
        this.registrarExpedienteService = registrarExpedienteService;
        this.mantenimientoAreaService = mantenimientoAreaService;
        this.hcUbicacionService = hcUbicacionService;
        this.mantenimientoHcGrupoService = mantenimientoGrupoService;
        this.reportService = reportService;
        this.expedienteService = expedienteService;
        this.servicioDTSService = servicioDTSService;
        this.reporteArrendamientoService = reporteArrendamientoService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(value = "/getAreas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Area> getAreas(HttpServletRequest request) {
        return mantenimientoAreaService.buscarAreas();
    }


    @GetMapping(value = "/getTiposDocumento", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoDocumento>> getTiposDocumento(HttpServletRequest request) {
        return mantenimientoProcesoService.getTiposDocumentos();
    }

    @GetMapping(value = "/getProcesosActivos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Proceso> getProcesosActivos(HttpServletRequest request) {
        return registrarExpedienteService.getProcesos();
    }

    @GetMapping(value = "/getSedes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Sede> getSedes(HttpServletRequest request) {
        return mantenimientoAreaService.buscarSedes();
    }

    @GetMapping(value = "/getUsuariosActivos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> getUsuarios(HttpServletRequest request) {
        return mantenimientoProcesoService.obtenerTodosUsuariosActivos();
    }

    @GetMapping(value = "/getEstadosDL", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getEstadosDL(HttpServletRequest request) {
        List<Map<String, Object>> estados = new ArrayList<>();

        Map<String, Object> e1 = new HashMap<String, Object>();
        e1.put("nombre", "Comunicacion a Interesados");
        e1.put("valor", Constantes.ESTADO_HC_COMUNICACION);
        estados.add(e1);

        Map<String, Object> e2 = new HashMap<String, Object>();
        e2.put("nombre", "Doc. Elaborado");
        e2.put("valor", Constantes.ESTADO_HC_ELABORADO);
        estados.add(e2);

        Map<String, Object> e3 = new HashMap<String, Object>();
        e3.put("nombre", "En Elaboracion");
        e3.put("valor", Constantes.ESTADO_HC_EN_ELABORACION);
        estados.add(e3);

        Map<String, Object> e4 = new HashMap<String, Object>();
        e4.put("nombre", "En Solicitud");
        e4.put("valor", Constantes.ESTADO_HC_EN_SOLICITUD);
        estados.add(e4);

        Map<String, Object> e5 = new HashMap<String, Object>();
        e5.put("nombre", "Enviado a Firma");
        e5.put("valor", Constantes.ESTADO_HC_ENVIADO_FIRMA);
        estados.add(e5);

        Map<String, Object> e6 = new HashMap<String, Object>();
        e6.put("nombre", "Enviado a Visado");
        e6.put("valor", Constantes.ESTADO_HC_ENVIADO_VISADO);
        estados.add(e6);

        Map<String, Object> e7 = new HashMap<String, Object>();
        e7.put("nombre", "Solicitud Enviada");
        e7.put("valor", Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
        estados.add(e7);

        Map<String, Object> e8 = new HashMap<String, Object>();
        e8.put("nombre", "Vencido");
        e8.put("valor", Constantes.ESTADO_HC_VENCIDO);
        estados.add(e8);

        Map<String, Object> e9 = new HashMap<String, Object>();
        e9.put("nombre", "Vigente");
        e9.put("valor", Constantes.ESTADO_HC_VIGENTE);
        estados.add(e9);

        Map<String, Object> e10 = new HashMap<String, Object>();
        e10.put("nombre", "Visado");
        e10.put("valor", Constantes.ESTADO_HC_VISADO);
        estados.add(e10);

        return new ResponseEntity<>(estados, HttpStatus.OK);
    }

    @GetMapping(value = "/getEstadosProcesos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getEstadosProcesos(HttpServletRequest request) {
        List<Map<String, Object>> estados = new ArrayList<>();

        Map<String, Object> e1 = new HashMap<String, Object>();
        e1.put("nombre", "Comunicacion a Interesados");
        e1.put("valor", Constantes.ESTADO_HC_COMUNICACION);
        estados.add(e1);

        Map<String, Object> e2 = new HashMap<String, Object>();
        e2.put("nombre", "Doc. Elaborado");
        e2.put("valor", Constantes.ESTADO_HC_ELABORADO);
        estados.add(e2);

        Map<String, Object> e3 = new HashMap<String, Object>();
        e3.put("nombre", "En Elaboracion");
        e3.put("valor", Constantes.ESTADO_HC_EN_ELABORACION);
        estados.add(e3);

        Map<String, Object> e4 = new HashMap<String, Object>();
        e4.put("nombre", "En Solicitud");
        e4.put("valor", Constantes.ESTADO_HC_EN_SOLICITUD);
        estados.add(e4);

        Map<String, Object> e5 = new HashMap<String, Object>();
        e5.put("nombre", "Enviado a Firma");
        e5.put("valor", Constantes.ESTADO_HC_ENVIADO_FIRMA);
        estados.add(e5);

        Map<String, Object> e6 = new HashMap<String, Object>();
        e6.put("nombre", "Enviado a Visado");
        e6.put("valor", Constantes.ESTADO_HC_ENVIADO_VISADO);
        estados.add(e6);

        Map<String, Object> e7 = new HashMap<String, Object>();
        e7.put("nombre", "Solicitud Enviada");
        e7.put("valor", Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
        estados.add(e7);

        Map<String, Object> e8 = new HashMap<String, Object>();
        e8.put("nombre", "Vencido");
        e8.put("valor", Constantes.ESTADO_HC_VENCIDO);
        estados.add(e8);

        Map<String, Object> e9 = new HashMap<String, Object>();
        e9.put("nombre", "Vigente");
        e9.put("valor", Constantes.ESTADO_HC_VIGENTE);
        estados.add(e9);

        Map<String, Object> e10 = new HashMap<String, Object>();
        e10.put("nombre", "Visado");
        e10.put("valor", Constantes.ESTADO_HC_VISADO);
        estados.add(e10);

        List<Map<String, Object>> cEstados = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> map : estados) {
            if (!map.containsValue('T') && !map.containsValue('N')) {
                cEstados.add(map);
            }
        }

        return new ResponseEntity<>(cEstados, HttpStatus.OK);
    }

    @GetMapping(value = "/getGruposActivos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<HcGrupo> getGruposActivos(HttpServletRequest request) {
        return this.mantenimientoHcGrupoService.obtenerActivos();
    }


    @GetMapping(value = "/documentosArea", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<DocumentoPorAreaBean>> getDocumentosByArea(HttpServletRequest request, DocumentoAreaFilter documentoAreaFilter) {
        LOGGER.info(documentoAreaFilter.toString());
        return new ResponseEntity<>(this.reportService.filtrarDocumentosPorArea(documentoAreaFilter), HttpStatus.OK);
    }

    @GetMapping(value = "/buscarDocumentosLegalesPorAbogadoResponsable", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<DocumentoLegalReporteBean>> getDocumentosPorAbogadoResponsable(HttpServletRequest request, @RequestParam(value = "idAbogadoResponsable", required = false) Integer idAbogadoResponsable) {
        return new ResponseEntity<>(this.reportService.filtrarDocumentosPorAbogadoResponsable(idAbogadoResponsable), HttpStatus.OK);
    }

    @GetMapping(value = "/buscarAlarmaContratos", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<DocumentoLegalAlarmaContratoBean>> buscarAlarmaContratos(HttpServletRequest request, AlarmaContratoFilter filter) {
        LOGGER.info(filter.toString());
        return new ResponseEntity<>(this.reportService.buscarAlarmasContrato(filter), HttpStatus.OK);
    }

    @GetMapping(value = "/filtrarReporteR4", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<DatosR4>> getReporteR4(HttpServletRequest request, ReportesR4Filter filter) {
        LOGGER.info(filter.toString());
        return new ResponseEntity<>(this.reportService.filtrarReporte4(filter), HttpStatus.OK);
    }


    @GetMapping(value = "/expedientesArea", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<ExpedientePorAreaBean>> getExpedientesPorArea(HttpServletRequest request, ExpedienteAreaFilter filter) {
        LOGGER.info(filter.toString());
        return new ResponseEntity<>(this.expedienteService.filterExpedientesByArea(filter), HttpStatus.OK);
    }

    @GetMapping(value = "/reporteServicio", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<ServicioDTS>> getReporteServicio(HttpServletRequest request, ServicioFilter filter) {
        LOGGER.info(filter.toString());
        return new ResponseEntity<>(this.servicioDTSService.filtrarServicios(filter), HttpStatus.OK);
    }

    @GetMapping(value = "/reporteArrendamiento", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<ArrendamientoReporteBean>> getReporteArrendamiento(HttpServletRequest request, ServicioFilter filter) {
        String token = request.getHeader("Authorization").substring(7);
        LOGGER.info(filter.toString());
        try {
            Integer idUsuario = jwtTokenUtil.getIdUserFromToken(token);
            List<ArrendamientoReporteBean> reporte = this.reporteArrendamientoService.buscarDocumentosLegales(
                    filter.getNum(),
                    null,
                    null,
                    DateUtil.convertStringToDate(filter.getFVI(), DateUtil.FORMAT_DATE),
                    DateUtil.convertStringToDate(filter.getFVF(), DateUtil.FORMAT_DATE),
                    Integer.valueOf(filter.getIdP()),
                    Integer.valueOf(filter.getIdC()),
                    Integer.valueOf(filter.getIdA()),
                    filter.getTUb(),
                    Integer.valueOf(filter.getIdU()),
                    Integer.valueOf(filter.getIdCo()),
                    Double.valueOf(filter.getMD()),
                    Double.valueOf(filter.getMH()),
                    Integer.valueOf(filter.getIdTC()),
                    filter.getEst().toCharArray()[0],
                    new ArrayList<>(),
                    false,
                    idUsuario
            );
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping(value = "/reporteSeguimientoProcesos", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<SeguimientoProcesosReporteBean>> buscarSeguimientoProcesosAll(HttpServletRequest request, @RequestBody SeguimientoProcesosFilter filter) {
        LOGGER.info(filter.toString());
        try {
            return new ResponseEntity<>(this.reportService.filtrarSeguimientoProcesos(filter), HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
