package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.dashboard.abogado.ClienteInternoBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.EstadoSolicitudBean;
import com.hochschild.speed.back.model.bean.dashboard.abogado.GeneralBean;

public interface DashboardPracticanteDao {
    GeneralBean solicitudesGenerales(Integer tipo, String usuario);
    EstadoSolicitudBean solicitudesPorAbogadoResponsable(Integer tipo, String usuario);
    ClienteInternoBean clientesInternos(Integer tipo, String usuario);
}
