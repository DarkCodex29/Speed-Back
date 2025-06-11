package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.ItemComboBean;
import com.hochschild.speed.back.model.domain.speed.HcCompania;
import com.hochschild.speed.back.model.filter.HcCompaniaFilter;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HcCompaniaService {
    List<HcCompania> find(HcCompaniaFilter filter);

    public ResponseEntity<List<ItemComboBean>> listarPorPais(HcCompaniaFilter filter);
}
