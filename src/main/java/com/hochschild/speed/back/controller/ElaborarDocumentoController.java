package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.elaborarDocumento.DocumentoInfo;
import com.hochschild.speed.back.model.bean.elaborarDocumento.GuardarBorradorBean;
import com.hochschild.speed.back.model.bean.elaborarDocumento.UsuarioDestinatarioBean;
import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModelFile;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.ElaborarDocumentoService;
import com.hochschild.speed.back.service.UsuarioService;
import com.hochschild.speed.back.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/elaborarDocumento")
public class ElaborarDocumentoController {
    private final Logger LOGGER = Logger.getLogger(ElaborarDocumentoController.class.getName());
    private final CommonBusinessLogicService commonBusinessLogicService;

    private final ElaborarDocumentoService elaborarDocumentoService;

    private final UsuarioService usuarioService;
    private final JwtTokenUtil jwtTokenUtil;

    public ElaborarDocumentoController(CommonBusinessLogicService commonBusinessLogicService, ElaborarDocumentoService elaborarDocumentoService, UsuarioService usuarioService, JwtTokenUtil jwtTokenUtil) {
        this.commonBusinessLogicService = commonBusinessLogicService;
        this.elaborarDocumentoService = elaborarDocumentoService;
        this.usuarioService = usuarioService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @RequestMapping(value = "/abrirDialog", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentoInfo> getInfoDocument(@RequestParam(name = "id") Integer id, HttpServletRequest request) {
        DocumentoInfo documentoInfo = new DocumentoInfo();
        try {
            documentoInfo = elaborarDocumentoService.buscarDestinatariosDefecto(id);
            return new ResponseEntity<>(documentoInfo, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(documentoInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public ResponseEntity<ResponseModelFile> uploadFiles(@RequestPart MultipartFile archivo,@RequestPart String rename, HttpServletRequest request) {
        LOGGER.info(archivo.getSize());
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = Integer.valueOf(String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idUsuario")));
        ResponseModelFile responseModel = elaborarDocumentoService.subirArchivo(archivo, idUsuario,rename);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }


    @ResponseBody
    @RequestMapping(value = "/buscarUsuariosActivos", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> getUsuariosActivos(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = Integer.valueOf(String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idUsuario")));
        return new ResponseEntity<>(commonBusinessLogicService.buscarUsuariosActivos(), HttpStatus.OK);
    }

    @RequestMapping(value = "/buscarUsuariosActivosBy", method = RequestMethod.GET)
    @ResponseBody
    public List<UsuarioDestinatarioBean> buscarUsuariosActivosByNombre(@RequestParam("term") String term) {
        List<UsuarioDestinatarioBean> usuariosResult = new ArrayList<UsuarioDestinatarioBean>();
        if (StringUtils.isNotEmpty(term)) {
            List<Usuario> usuarios = commonBusinessLogicService.buscarUsuarios("%" + term + "%");
            usuariosResult = elaborarDocumentoService.buscarUsuariosUbicacion(usuarios);
        }
        return usuariosResult;
    }

    @ResponseBody
    @RequestMapping(value = "/guardarBorrador", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> guardarBorrador(@RequestBody GuardarBorradorBean guardarBorradorBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = Integer.valueOf(String.valueOf(jwtTokenUtil.getClaimFromToken(token, "idUsuario")));
        Usuario usuario = usuarioService.findUsuarioById(idUsuario);
        Documento documento = commonBusinessLogicService.obtenerDocumento(guardarBorradorBean.getId());
        if (usuario != null) {
            try {
                Map<String, Object> response = elaborarDocumentoService.guardarBorrador(documento, guardarBorradorBean.getArchivo(), guardarBorradorBean.getIdDestinatarios(), usuario, guardarBorradorBean.getEnviadoC());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }catch (Exception e){
                Map<String, Object> result = new HashMap<>();
                result.put("resultado", "error" + e.getMessage());
                result.put("mensaje", "Ocurrio un error al adjuntar el borrador");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("resultado", "error");
        result.put("mensaje", "Ocurrio un error al adjuntar el borrador");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
