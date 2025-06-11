package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.usuariosPorAlarma.UsuariosAlarmaBean;
import com.hochschild.speed.back.service.UsuarioPorAlarmaService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/usuariosPorAlarma")
public class UsuarioPorAlarmaController {
    private static final Logger LOGGER = Logger.getLogger(UsuarioPorAlarmaController.class.getName());
    private final UsuarioPorAlarmaService usuarioPorAlarmaService;

    public UsuarioPorAlarmaController(UsuarioPorAlarmaService usuarioPorAlarmaService) {
        this.usuarioPorAlarmaService = usuarioPorAlarmaService;
    }

    @GetMapping(value = "/{idAlarma}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuariosAlarmaBean>> obtenerUsuariosPorAlarma(@PathVariable("idAlarma") Integer idAlarma, HttpServletRequest request) {

        List<UsuariosAlarmaBean> result = usuarioPorAlarmaService.obtenerUsuariosPorAlarma(idAlarma);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
