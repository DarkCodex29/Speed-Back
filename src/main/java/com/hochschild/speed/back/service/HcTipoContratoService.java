package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HcTipoContratoService {
    ResponseEntity<List<HcTipoContrato>> list();
}
