package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.bean.ItemComboBean;
import com.hochschild.speed.back.model.domain.speed.HcCompania;
import com.hochschild.speed.back.model.filter.HcCompaniaFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.HcCompaniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI S.A.C.
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/hcCompania")
public class HcCompaniaController {

    private final HcCompaniaService hcCompaniaService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public HcCompaniaController(HcCompaniaService hcCompaniaService, JwtTokenUtil jwtTokenUtil) {
        this.hcCompaniaService = hcCompaniaService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcCompania>> find(HcCompaniaFilter hcCompaniaFilter, HttpServletRequest request) {
        List<HcCompania> result = hcCompaniaService.find(hcCompaniaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/listarPorPais", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<ItemComboBean>> listarPorPais(
            @RequestBody HcCompaniaFilter hcCompaniaFilter
            , HttpServletRequest request) {
        return hcCompaniaService.listarPorPais(hcCompaniaFilter);
    }

}