package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.bandejaEntrada.ArchivarBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.ArchivarFilter;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosArchivarBean;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.repository.speed.TrazaRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.ArchivarExpedienteService;
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

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */

@RestController
@RequestMapping("/archivarExpediente")
public class ArchivarExpedienteController {
    private final ArchivarExpedienteService archivarExpedienteService;
    private final TrazaRepository trazaRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ArchivarExpedienteController(ArchivarExpedienteService archivarExpedienteService,
                                        TrazaRepository trazaRepository,
                                        UsuarioRepository usuarioRepository,
                                        JwtTokenUtil jwtTokenUtil) {
        this.archivarExpedienteService = archivarExpedienteService;
        this.trazaRepository = trazaRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/archivar", method = RequestMethod.POST)
    public ResponseEntity<Integer> archivar(@RequestBody ArchivarBean archivarBean, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

        Integer result = archivarExpedienteService.archivarExpediente(archivarBean, idUsuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/datosArchivar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<DatosArchivarBean> botonDatosArchivar(ArchivarFilter archivarFilter, HttpServletRequest request) {

        DatosArchivarBean result = archivarExpedienteService.botonDatosArchivar(archivarFilter.getIdExpediente(), archivarFilter.getEliminarSolicitud());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/verificarMasivo", method = RequestMethod.GET)
    public Map<String, Object> verificarMasivo(Integer[] idTrazas) {

        Map<String, Object> salida = new HashMap<String, Object>();
        List<Traza> trazas = archivarExpedienteService.obtenerTrazas(idTrazas);
        List<String> expedientes = archivarExpedienteService.expedienteDerivacionSimple(trazas);

        if (expedientes.size() > 0) {
            salida.put("exito", false);
            String mensaje = "<p>Los siguientes expedientes deben provenir de una derivaci\u00f3n simple:</p><ul>";
            for (String e : expedientes) {
                mensaje += "<li>" + e + "</li>";
            }
            salida.put("mensaje", mensaje + "</ul>");
        } else {
            expedientes = archivarExpedienteService.expedienteDerivadoRol(trazas);
            if (expedientes.size() > 0) {
                salida.put("exito", false);
                String mensaje = "<p>Los siguientes expedientes fueron derivados a un rol:</p><ul>";
                for (String e : expedientes) {
                    mensaje += "<li>" + e + "</li>";
                }
                salida.put("mensaje", mensaje + "</ul>");
            } else {
                expedientes = archivarExpedienteService.expedienteDerivacionMultiple(trazas);
                if (expedientes.size() > 0) {
                    salida.put("exito", false);
                    String mensaje = "<p>Los siguientes expedientes fueron derivados de forma m\u00faltiple:</p><ul>";
                    for (String e : expedientes) {
                        mensaje += "<li>" + e + "</li>";
                    }
                    salida.put("mensaje", mensaje + "</ul>");
                } else {
                    expedientes = archivarExpedienteService.expedienteProcesoWorkflow(trazas);
                    if (expedientes.size() > 0) {
                        salida.put("exito", false);
                        String mensaje = "<p>Los siguientes expedientes pertenecen a un proceso de tipo Workflow:</p><ul>";
                        for (String e : expedientes) {
                            mensaje += "<li>" + e + "</li>";
                        }
                        salida.put("mensaje", mensaje + "</ul>");
                    } else {
                        expedientes = archivarExpedienteService.expedienteProcesoIntalio(trazas);
                        if (expedientes.size() > 0) {
                            salida.put("exito", false);
                            String mensaje = "<p>Los siguientes expedientes pertenecen a un proceso de tipo Intalio:</p><ul>";
                            for (String e : expedientes) {
                                mensaje += "<li>" + e + "</li>";
                            }
                            salida.put("mensaje", mensaje + "</ul>");
                        } else {
                            salida.put("exito", true);
                        }
                    }
                }
            }
        }
        return salida;
    }

    @RequestMapping(value = "/expedientes", method = RequestMethod.GET)
    @ResponseBody
    public List<String> obtenerExpedientes(Integer[] idTrazas) {
        List<String> expedientes = new ArrayList<String>();
        for (Integer idTraza : idTrazas) {
            expedientes.add(archivarExpedienteService.obtenerExpedientePorTraza(idTraza).getNumero());
        }
        return expedientes;
    }

    @RequestMapping(value = "/masivo", method = RequestMethod.POST)
    @ResponseBody
    public Boolean archivarMasivo(Integer[] idTrazas, String observacion, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        Usuario usuario = usuarioRepository.findById(idUsuario);

        if (usuario != null) {
            for (Integer idTraza : idTrazas) {
                Traza traza = trazaRepository.findById(idTraza);
                ArchivarBean archivarBean = new ArchivarBean();
                archivarBean.setIdExpediente(traza.getExpediente().getId());
                archivarBean.setAccion(null);
                archivarBean.setObservacion(observacion);
                archivarBean.setEliminarSolicitud(false);
                archivarExpedienteService.archivarExpediente(archivarBean, usuario.getId());
            }
            return true;
        }
        return false;
    }
}