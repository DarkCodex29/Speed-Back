package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.config.AlfrescoConfig;
import com.hochschild.speed.back.dao.ElaborarDocumentoDao;
import com.hochschild.speed.back.model.domain.speed.Archivo;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.service.RevisarDocumentoService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.Map;


/**
 * @author CAPSULA DIGITAL
 * @since 24/07/2023
 */
@RestController
@RequestMapping("/descargarArchivo")
public class DescargarArchivoController {
    private static final Logger LOGGER = Logger.getLogger(DescargarArchivoController.class.getName());
    private final AlfrescoConfig alfrescoConfig;
    private final JwtTokenUtil jwtTokenUtil;
    private final RevisarDocumentoService revisarDocumentoService;
    private final AlfrescoService alfrescoService;
    private final UsuarioRepository usuarioRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final ElaborarDocumentoDao elaborarDocumentoDao;
    private final ParametroRepository parametroRepository;


    public DescargarArchivoController(AlfrescoConfig alfrescoConfig, JwtTokenUtil jwtTokenUtil,
                                      RevisarDocumentoService revisarDocumentoService,
                                      AlfrescoService alfrescoService,
                                      UsuarioRepository usuarioRepository,
                                      HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                      ElaborarDocumentoDao elaborarDocumentoDao,
                                      ParametroRepository parametroRepository) {
        this.alfrescoConfig = alfrescoConfig;
        this.jwtTokenUtil = jwtTokenUtil;
        this.revisarDocumentoService = revisarDocumentoService;
        this.alfrescoService = alfrescoService;
        this.usuarioRepository = usuarioRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.elaborarDocumentoDao = elaborarDocumentoDao;
        this.parametroRepository = parametroRepository;
    }

    @ResponseBody
    @RequestMapping(value = "/{idExpediente}/{idArchivo}", method = RequestMethod.GET)
    public void descargar(@PathVariable("idArchivo") Integer idArchivo, @PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request, final HttpServletResponse httpServletResponse) {
        descargar(idArchivo, idExpediente, null, request, httpServletResponse);
    }


    @ResponseBody
    @RequestMapping(value = "/{idArchivo}/{idExpediente}/{version}", method = RequestMethod.GET)
    public void descargar(@PathVariable("idArchivo") Integer idArchivo, @PathVariable("idExpediente") Integer idExpediente, @PathVariable("version") String version, HttpServletRequest request, final HttpServletResponse httpServletResponse) {


        if (idExpediente != null) {
            String token = request.getHeader("Authorization").substring(7);
            Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

            Usuario usuario = usuarioRepository.findById(idUsuario);

            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(idExpediente);

            if (hcDocumentoLegal != null && hcDocumentoLegal.getId() != null) {

                hcDocumentoLegal.setFechaMovimiento(new Date());
                hcDocumentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO, elaborarDocumentoDao.ubicacionUsuarioBean(usuario.getUsuario())));

                hcDocumentoLegalRepository.save(hcDocumentoLegal);
            }
        }

        Archivo archivo = revisarDocumentoService.obtenerArchivo(idArchivo);

        if (archivo != null) {
            String contentType = null;
            InputStream in = null;
            if (alfrescoConfig.getHabilitado()) {
                Map<String, Object> file = alfrescoService.obtenerArchivo(archivo, version);
                if (file != null) {
                    contentType = (String) file.get("mime");
                    in = (InputStream) file.get("stream");
                }
            } else if (!AppUtil.checkNullOrEmpty(archivo.getRutaLocal())) {
                File file = new File(archivo.getRutaLocal());
                try {
                    contentType = Files.probeContentType(file.toPath());
                    in = new FileInputStream(file);
                } catch (IOException e) {
                    LOGGER.info("Error leyendo archivo local", e);
                }
            }
            if (contentType != null && in != null) {
                httpServletResponse.setHeader("Content-Disposition", "filename=\"" + archivo.getNombre() + "\"");
                LOGGER.info("Content-Type: [" + contentType + "]");
                httpServletResponse.setContentType(contentType);
                OutputStream out;
                try {
                    out = httpServletResponse.getOutputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.info("No se pudo descargar el archivo", e);
                    try {
                        httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException ioe) {
                        LOGGER.info("Error enviando respuesta", ioe);
                    }
                }
            } else {
                try {
                    httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.info("Error enviando respuesta", e);
                }
            }
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/usuario/{idExpediente}/{idArchivo}/{idUsuario}", method = RequestMethod.GET)
    public void descargarArchivoUsuario(@PathVariable("idArchivo") Integer idArchivo, 
    		@PathVariable("idExpediente") Integer idExpediente, 
    		@PathVariable("idUsuario") String idUsuario, 
    		HttpServletRequest request, final HttpServletResponse httpServletResponse) {


        if (idExpediente != null) {
            Usuario usuario = usuarioRepository.obtenerPorUsuario(idUsuario);

            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(idExpediente);

            if (hcDocumentoLegal != null && hcDocumentoLegal.getId() != null) {

                hcDocumentoLegal.setFechaMovimiento(new Date());
                hcDocumentoLegal.setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO, elaborarDocumentoDao.ubicacionUsuarioBean(usuario.getUsuario())));

                hcDocumentoLegalRepository.save(hcDocumentoLegal);
            }
        }

        Archivo archivo = revisarDocumentoService.obtenerArchivo(idArchivo);

        if (archivo != null) {
            String contentType = null;
            InputStream in = null;
            if (alfrescoConfig.getHabilitado()) {
                Map<String, Object> file = alfrescoService.obtenerArchivo(archivo, null);
                if (file != null) {
                    contentType = (String) file.get("mime");
                    in = (InputStream) file.get("stream");
                }
            } else if (!AppUtil.checkNullOrEmpty(archivo.getRutaLocal())) {
                File file = new File(archivo.getRutaLocal());
                try {
                    contentType = Files.probeContentType(file.toPath());
                    in = new FileInputStream(file);
                } catch (IOException e) {
                    LOGGER.info("Error leyendo archivo local", e);
                }
            }
            if (contentType != null && in != null) {
                httpServletResponse.setHeader("Content-Disposition", "filename=\"" + archivo.getNombre() + "\"");
                LOGGER.info("Content-Type: [" + contentType + "]");
                httpServletResponse.setContentType(contentType);
                OutputStream out;
                try {
                    out = httpServletResponse.getOutputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.info("No se pudo descargar el archivo", e);
                    try {
                        httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException ioe) {
                        LOGGER.info("Error enviando respuesta", ioe);
                    }
                }
            } else {
                try {
                    httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.info("Error enviando respuesta", e);
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/plantilla", method = RequestMethod.POST)
    public void descargarByRuta(@RequestBody Map<String, String> archivo, final HttpServletResponse response) {
        try {
            String ruta = archivo.get("ruta");
            String nombre = ruta.substring(ruta.lastIndexOf("/") + 1);
            String path = ruta.replace("/" + nombre, "");
            Map<String, Object> plantillaFromAlfresco = alfrescoService.descargarArchivo(path, nombre);
            String contentType = (String) plantillaFromAlfresco.get("mime");
            InputStream in = (InputStream) plantillaFromAlfresco.get("stream");
            if (contentType != null && in != null) {
                response.setHeader("Content-Disposition", "attachment; filename=" + nombre);
                LOGGER.info("Content-Type: [" + contentType + "]");
                response.setContentType("application/msword");
                try {
                    OutputStream out = response.getOutputStream();
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = in.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.info("No se pudo descargar el archivo", e);
                    try {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException ioe) {
                        LOGGER.info("Error enviando respuesta", ioe);
                    }
                }
            } else {
                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.info("Error enviando respuesta", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
