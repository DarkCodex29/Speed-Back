package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.PlantillaBean;
import com.hochschild.speed.back.model.domain.speed.HcPlantilla;
import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.model.filter.mantenimiento.PlantillaFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoPlantillaService;
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
@RequestMapping("/plantilla")
public class PlantillaController {
    private Logger LOGGER = LoggerFactory.getLogger(PlantillaController.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoPlantillaService mantenimientoPlantillaService;

    public PlantillaController(JwtTokenUtil jwtTokenUtil, MantenimientoPlantillaService mantenimientoPlantillaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoPlantillaService = mantenimientoPlantillaService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HcPlantilla> find(@PathVariable("id") Integer id) {
        HcPlantilla result = mantenimientoPlantillaService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcPlantilla>> list(@RequestBody PlantillaFilter plantillaFilter){
        List<HcPlantilla> result = mantenimientoPlantillaService.list(plantillaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoContratos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcTipoContrato>> listTipoContratos(){
        List<HcTipoContrato> result = mantenimientoPlantillaService.listContratos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody PlantillaBean plantillaBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoPlantillaService.save(plantillaBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}