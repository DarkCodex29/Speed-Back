package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.service.ElaborarDocumentoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/estadoBandejaAbogados")
public class EstadoBandejaAbogadosController {

    final ElaborarDocumentoService elaborarDocumentoService;

    public EstadoBandejaAbogadosController(ElaborarDocumentoService elaborarDocumentoService) {
        this.elaborarDocumentoService = elaborarDocumentoService;
    }

    @GetMapping(value = "/getAnios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Integer> getAnios() {
        return elaborarDocumentoService.listaAnios();
    }
}
