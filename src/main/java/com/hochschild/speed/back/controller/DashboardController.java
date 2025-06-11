package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.dao.DashboardAbogadoDao;
import com.hochschild.speed.back.dao.DashboardAdcDao;
import com.hochschild.speed.back.dao.DashboardAreaDao;
import com.hochschild.speed.back.model.bean.DashboardBean;
import com.hochschild.speed.back.model.bean.dashboard.ModeloDocumentoBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.ClienteInternoBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.EstadoSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralElaboradoBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.GeneralAdcBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.ProcesoFirmaBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.ProcesoVisadoBean;
import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudVigenteBean;
import com.hochschild.speed.back.model.bean.dashboard.area.GeneralAreaBean;
import com.hochschild.speed.back.model.bean.dashboard.area.SolicitudBean;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final DashboardAbogadoDao dashboardAbogadoDao;
    private final DashboardAdcDao dashboardAdcDao;
    private final DashboardAreaDao dashboardAreaDao;
    private final JwtTokenUtil jwtTokenUtil;

    public DashboardController(JwtTokenUtil jwtTokenUtil, DashboardService dashboardService, DashboardAbogadoDao dashboardAbogadoDao, DashboardAdcDao dashboardAdcDao, DashboardAreaDao dashboardAreaDao) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.dashboardService = dashboardService;
        this.dashboardAbogadoDao = dashboardAbogadoDao;
        this.dashboardAdcDao = dashboardAdcDao;
        this.dashboardAreaDao = dashboardAreaDao;
    }

    @RequestMapping(value = "/validarAcceso",  method = RequestMethod.GET)
    public ResponseEntity<DashboardBean> validarAcceso(HttpServletRequest request) {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardService.validarAcceso(usuario);
    }

    @RequestMapping(value = "/solicitudesGenerales",  method = RequestMethod.GET)
    public List<GeneralBean> solicitudesGenerales(HttpServletRequest request, @RequestParam(name = "tipo") Integer tipo) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAbogadoDao.solicitudesGenerales(tipo, usuario);
    }

    @RequestMapping(value = "/solicitudesGeneralesElaborado",  method = RequestMethod.GET)
    public List<GeneralElaboradoBean> solicitudesGeneralesElaborado(HttpServletRequest request, @RequestParam(name = "tipo") Integer tipo) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAbogadoDao.solicitudesGeneralesElaborado(tipo, usuario);
    }

    @RequestMapping(value = "/solicitudesPorAbogadoResponsable",  method = RequestMethod.GET)
    public List<EstadoSolicitudBean> solicitudesPorAbogadoResponsable(HttpServletRequest request, @RequestParam(name = "tipo") Integer tipo) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAbogadoDao.solicitudesPorAbogadoResponsable(tipo, usuario);
    }

    @RequestMapping(value = "/clientesInternos",  method = RequestMethod.GET)
    public List<ClienteInternoBean> clientesInternos(HttpServletRequest request, @RequestParam(name = "tipo") Integer tipo) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAbogadoDao.clientesInternos(tipo, usuario);
    }

    @RequestMapping(value = "/solicitudesGeneralesAdc",  method = RequestMethod.GET)
    public List<GeneralAdcBean> solicitudesGenerales() {
        return dashboardAdcDao.solicitudesGenerales();
    }

    @RequestMapping(value = "/solicitudesPorGrupoAdc",  method = RequestMethod.GET)
    public List<GeneralAdcBean> solicitudesPorGrupo() {
        return dashboardAdcDao.solicitudesPorGrupo();
    }

    @RequestMapping(value = "/procesoPorFirmaAdc",  method = RequestMethod.GET)
    public List<ProcesoFirmaBean> procesoPorFirma() {
        return dashboardAdcDao.procesoPorFirma();
    }

    @RequestMapping(value = "/procesoPorVisadoAdc",  method = RequestMethod.GET)
    public List<ProcesoVisadoBean> procesoPorVisado() {
        return dashboardAdcDao.procesoPorVisado();
    }

    @RequestMapping(value = "/solicitudesGeneralesArea",  method = RequestMethod.GET)
    public List<GeneralAreaBean> solicitudesGenerales(HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAreaDao.solicitudesGenerales(usuario);
    }

    @RequestMapping(value = "/solicitudesPorAbogadoArea",  method = RequestMethod.GET)
    public List<AreaSolicitudBean> solicitudesPorAbogado(HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAreaDao.solicitudesPorAbogado(usuario);
    }

    @RequestMapping(value = "/solicitudesVigentesArea",  method = RequestMethod.GET)
    public List<AreaSolicitudVigenteBean> solicitudesVigentes(HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAreaDao.solicitudesVigente(usuario);
    }

    @RequestMapping(value = "/solicitudesArea",  method = RequestMethod.GET)
    public List<SolicitudBean> solicitudesArea(HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        String usuario = jwtTokenUtil.getUsernameFromToken(token);
        return dashboardAreaDao.solicitudes(usuario);
    }

    @RequestMapping(value = "/modeloDocumentos",  method = RequestMethod.GET)
    public ResponseEntity<List<ModeloDocumentoBean>> modeloDocumentos() {
        return dashboardService.obtenerModeloDocumentos();
    }
}