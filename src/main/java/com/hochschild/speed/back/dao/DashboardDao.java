package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.DashboardBean;
import com.hochschild.speed.back.model.bean.dashboard.ModeloDocumentoBean;

import java.util.List;

public interface DashboardDao {
    DashboardBean validarAcceso(String usuario);

    List<ModeloDocumentoBean> obtenerModeloDocumentos();
}
