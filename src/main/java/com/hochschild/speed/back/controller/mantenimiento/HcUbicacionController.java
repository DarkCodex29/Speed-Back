package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.HcUbicacionBean;
import com.hochschild.speed.back.model.domain.speed.HcTipoUbicacion;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.service.HcUbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hcUbicacion")
public class HcUbicacionController {

    @Autowired
    private HcUbicacionService hcUbicacionService;

    @ResponseBody
    @RequestMapping(value = "/guardarHcUbicacionModal", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarHcUbicacionModal(@RequestBody HcUbicacionBean hcUbicacionBean) {

        ResponseModel responseModel = hcUbicacionService.guardarHcUbicacion(hcUbicacionBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }


    @ResponseBody
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/obtenerHcTipoUbicacion", method = RequestMethod.GET)
    public ResponseEntity<List<HcTipoUbicacion>> obtenerHcTipoUbicacion() {


        return hcUbicacionService.obtenerHcTipoUbicacion();
    }


    @ResponseBody
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/listarUbicaciones", method = RequestMethod.GET)
    public ResponseEntity<List<HcUbicacion>> listarUbicaciones() {


        return hcUbicacionService.listarUbicaciones();
    }
}
