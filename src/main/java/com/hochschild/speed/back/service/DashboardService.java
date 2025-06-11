package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.DashboardBean;
import com.hochschild.speed.back.model.bean.dashboard.ModeloDocumentoBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DashboardService {
    ResponseEntity<DashboardBean> validarAcceso(String usuario);

    ResponseEntity<List<ModeloDocumentoBean>> obtenerModeloDocumentos();
}
