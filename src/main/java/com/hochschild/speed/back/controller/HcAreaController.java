package com.hochschild.speed.back.controller;

import com.hochschild.speed.back.model.domain.speed.HcArea;
import com.hochschild.speed.back.model.filter.HcAreaFilter;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.HcAreaService;
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
@RequestMapping("/hcArea")
public class HcAreaController {

    private final HcAreaService hcAreaService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public HcAreaController(HcAreaService hcAreaService, JwtTokenUtil jwtTokenUtil) {
        this.hcAreaService = hcAreaService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HcArea>> find(HcAreaFilter hcAreaFilter, HttpServletRequest request) {
        List<HcArea> result = hcAreaService.find(hcAreaFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}