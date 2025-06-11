package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.dashboard.abogado.ClienteInternoBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.EstadoSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralElaboradoBean;

import java.util.List;

public interface DashboardAbogadoDao {
    List<GeneralBean> solicitudesGenerales(Integer tipo, String usuario);
    List<EstadoSolicitudBean> solicitudesPorAbogadoResponsable(Integer tipo, String usuario);
    List<ClienteInternoBean> clientesInternos(Integer tipo, String usuario);
    List<GeneralElaboradoBean> solicitudesGeneralesElaborado(Integer tipo, String usuario);
}
