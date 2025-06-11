package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.area.AreaSolicitudVigenteBean;
import com.hochschild.speed.back.model.bean.dashboard.area.GeneralAreaBean;
import com.hochschild.speed.back.model.bean.dashboard.area.SolicitudBean;

import java.util.List;

public interface DashboardAreaDao {
    List<GeneralAreaBean> solicitudesGenerales(String usuario);
    List<AreaSolicitudBean> solicitudesPorAbogado(String usuario);
    List<AreaSolicitudVigenteBean> solicitudesVigente(String usuario);
    List<SolicitudBean> solicitudes(String usuario);
}
