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
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
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
    public void descargar(@PathVariable("idArchivo") Integer idArchivo,
            @PathVariable("idExpediente") Integer idExpediente, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) {
        descargar(idArchivo, idExpediente, null, request, httpServletResponse);
    }

    @ResponseBody
    @RequestMapping(value = "/{idArchivo}/{idExpediente}/{version}", method = RequestMethod.GET)
    public void descargar(@PathVariable("idArchivo") Integer idArchivo,
            @PathVariable("idExpediente") Integer idExpediente, @PathVariable("version") String version,
            HttpServletRequest request, final HttpServletResponse httpServletResponse) {

        if (idExpediente != null) {
            String token = request.getHeader("Authorization").substring(7);
            Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");

            Usuario usuario = usuarioRepository.findById(idUsuario);

            HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(idExpediente);

            if (hcDocumentoLegal != null && hcDocumentoLegal.getId() != null) {

                hcDocumentoLegal.setFechaMovimiento(new Date());
                hcDocumentoLegal
                        .setUbicacionDocumento(parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO,
                                elaborarDocumentoDao.ubicacionUsuarioBean(usuario.getUsuario())));

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
    @RequestMapping(value = "/usuario/{idExpediente}/{idArchivo}", method = RequestMethod.GET)
    public void descargarArchivoUsuario(@PathVariable("idArchivo") Integer idArchivo,
            @PathVariable("idExpediente") Integer idExpediente,
            HttpServletRequest request, final HttpServletResponse httpServletResponse) {

        try {
            LOGGER.info("Iniciando descarga de archivo. IdExpediente: " + idExpediente + ", IdArchivo: " + idArchivo);

            if (idExpediente != null) {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    LOGGER.error("Token de autorización no encontrado o inválido");
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String token = authHeader.substring(7);
                Integer idUsuarioToken = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
                LOGGER.info("Usuario obtenido del token: " + idUsuarioToken);

                Usuario usuario = usuarioRepository.findById(idUsuarioToken);

                HcDocumentoLegal hcDocumentoLegal = hcDocumentoLegalRepository.findByIdExpediente(idExpediente);

                if (hcDocumentoLegal != null && hcDocumentoLegal.getId() != null) {
                    hcDocumentoLegal.setFechaMovimiento(new Date());
                    hcDocumentoLegal
                            .setUbicacionDocumento(
                                    parametroRepository.obtenerPorTipoValor(Constantes.PARAMETRO_SEGUIMIENTO,
                                            elaborarDocumentoDao.ubicacionUsuarioBean(usuario.getUsuario())));

                    hcDocumentoLegalRepository.save(hcDocumentoLegal);
                }
            }

            Archivo archivo = revisarDocumentoService.obtenerArchivo(idArchivo);
            LOGGER.info("Archivo obtenido: " + (archivo != null ? archivo.getNombre() : "null"));

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
                    LOGGER.error("ContentType o InputStream es null");
                    try {
                        httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException e) {
                        LOGGER.info("Error enviando respuesta", e);
                    }
                }
            } else {
                LOGGER.error("Archivo no encontrado con ID: " + idArchivo);
                try {
                    httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.error("Error enviando respuesta de archivo no encontrado", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error inesperado en descarga de archivo", e);
            try {
                httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ioe) {
                LOGGER.error("Error enviando respuesta de error interno", ioe);
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

    @ResponseBody
    @RequestMapping(value = "/publico/{idExpediente}/{idArchivo}/{hash}", method = RequestMethod.GET)
    public void descargarArchivoPublico(@PathVariable("idArchivo") Integer idArchivo,
            @PathVariable("idExpediente") Integer idExpediente,
            @PathVariable("hash") String hash,
            final HttpServletResponse httpServletResponse) {

        try {
            LOGGER.info("Descarga pública - IdExpediente: " + idExpediente + ", IdArchivo: " + idArchivo);

            // Validar hash (sin expiración)
            String hashEsperado = generarHashDescarga(idExpediente, idArchivo);
            if (!hash.equals(hashEsperado)) {
                LOGGER.error("Hash inválido para descarga pública");
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Hash inválido");
                return;
            }

            // Descargar archivo (misma lógica que método privado)
            Archivo archivo = revisarDocumentoService.obtenerArchivo(idArchivo);
            LOGGER.info("Archivo obtenido para descarga pública: " + (archivo != null ? archivo.getNombre() : "null"));

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
                    LOGGER.error("ContentType o InputStream es null");
                    try {
                        httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException e) {
                        LOGGER.info("Error enviando respuesta", e);
                    }
                }
            } else {
                LOGGER.error("Archivo no encontrado con ID: " + idArchivo);
                try {
                    httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    LOGGER.error("Error enviando respuesta de archivo no encontrado", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error inesperado en descarga pública", e);
            try {
                httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ioe) {
                LOGGER.error("Error enviando respuesta de error interno", ioe);
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/generarUrlSegura", method = RequestMethod.POST)
    public Map<String, String> generarUrlSegura(@RequestBody Map<String, Integer> request) {
        try {
            Integer idExpediente = request.get("idExpediente");
            Integer idArchivo = request.get("idArchivo");

            LOGGER.info("Generando URL segura - IdExpediente: " + idExpediente + ", IdArchivo: " + idArchivo);

            String hash = generarHashDescarga(idExpediente, idArchivo);

            // Construir URL base desde el request
            String baseUrl = "https://10.30.44.7/speed-back-demo"; // TODO: hacer configurable
            String urlSegura = baseUrl + "/descargarArchivo/publico/" + idExpediente + "/" + idArchivo + "/" + hash;

            Map<String, String> response = new HashMap<>();
            response.put("url", urlSegura);

            LOGGER.info("URL segura generada exitosamente");
            return response;

        } catch (Exception e) {
            LOGGER.error("Error generando URL segura", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error generando URL segura");
            return errorResponse;
        }
    }

    private String generarHashDescarga(Integer idExpediente, Integer idArchivo) {
        try {
            String secretKey = "SPEED_DOWNLOAD_SECRET_2024"; // TODO: mover a configuración
            String dataToHash = idExpediente + "-" + idArchivo + "-" + secretKey;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            LOGGER.error("Error generando hash", e);
            throw new RuntimeException("Error generando hash", e);
        }
    }
}
