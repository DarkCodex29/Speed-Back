package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.CampoDocumentoBean;
import com.hochschild.speed.back.model.bean.mantenimiento.TipoDocumentoBean;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import com.hochschild.speed.back.model.filter.mantenimiento.TipoDocumentoFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoTipoDocumentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/tipoDocumento")
public class TipoDocumentoController {
    private Logger LOGGER = LoggerFactory.getLogger(TipoDocumentoController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoTipoDocumentoService mantenimientoTipoDocumentoService;

    public TipoDocumentoController(JwtTokenUtil jwtTokenUtil, MantenimientoTipoDocumentoService mantenimientoTipoDocumentoService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoTipoDocumentoService = mantenimientoTipoDocumentoService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoDocumento> find(@PathVariable("id") Integer id) {
        TipoDocumento result = mantenimientoTipoDocumentoService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoDocumento>> list(@RequestBody TipoDocumentoFilter tipoDocumentoFilter){
        List<TipoDocumento> result = mantenimientoTipoDocumentoService.list(tipoDocumentoFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/campos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampoDocumentoBean>> listCampos(){
        List<CampoDocumentoBean> result = mantenimientoTipoDocumentoService.listCampos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/camposDisponibles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampoDocumentoBean>> camposDisponibles(@PathVariable("id") Integer id){
        List<CampoDocumentoBean> result = mantenimientoTipoDocumentoService.listCamposDisponibles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody TipoDocumentoBean tipoDocumentoBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoTipoDocumentoService.save(tipoDocumentoBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}