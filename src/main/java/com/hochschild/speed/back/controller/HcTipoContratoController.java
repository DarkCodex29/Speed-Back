package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.service.HcTipoContratoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/HcTipoContrato")
public class HcTipoContratoController {

    private final HcTipoContratoService hcTipoContratoService;

    public HcTipoContratoController(HcTipoContratoService hcTipoContratoService) {
        this.hcTipoContratoService = hcTipoContratoService;
    }


    //    @ResponseBody
//    @RequestMapping(value = "/listar", method = RequestMethod.POST)
    @ResponseBody
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public ResponseEntity<List<HcTipoContrato>> listar(HttpServletRequest request) {
        return hcTipoContratoService.list();

    }

}
