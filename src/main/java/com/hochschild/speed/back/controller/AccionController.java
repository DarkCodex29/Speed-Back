package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.HcPais;
import com.hochschild.speed.back.model.filter.HcPaisFilter;
import com.hochschild.speed.back.service.HcPaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/accion")
public class AccionController {

    private final HcPaisService hcPaisService;

    @Autowired
    public AccionController(HcPaisService hcPaisService) {
        this.hcPaisService = hcPaisService;
    }

    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List<HcPais>> find(HcPaisFilter hcPaisFilter, HttpServletRequest request) {
        List<HcPais> result = hcPaisService.find(hcPaisFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}