package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.verTraza.HistorialTrazaBean;
import com.hochschild.speed.back.service.VerTrazaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verTraza")
public class VerTrazaController {

    private final VerTrazaService verTrazaService;

    @Autowired
    public VerTrazaController(VerTrazaService verTrazaService) {
        this.verTrazaService = verTrazaService;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"idExpediente"})
    public ResponseEntity<HistorialTrazaBean> verTraza(@RequestParam("idExpediente") Integer idExpediente) {
        HistorialTrazaBean historialTrazaBean = verTrazaService.obtenerHistoriaTrazasPorExpediente(idExpediente);
        return new ResponseEntity<>(historialTrazaBean, historialTrazaBean.getHttpSatus());
    }
}
