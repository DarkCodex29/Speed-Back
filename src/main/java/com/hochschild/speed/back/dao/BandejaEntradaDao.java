package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.BandejaBean;
import com.hochschild.speed.back.model.bean.MisPendientesBean;
import com.hochschild.speed.back.model.bean.SolicitudesPorEnviarBean;
import com.hochschild.speed.back.model.filter.bandejaEntrada.BandejaEntradaFilter;
import com.hochschild.speed.back.model.filter.bandejaMisSolicitudes.BandejaMisSolicitudesFilter;
import com.hochschild.speed.back.model.filter.bandejaPendientes.BandejaPendientesFilter;
import com.hochschild.speed.back.model.filter.bandejaSolicitudesPorEnviar.BandejaSolicitudesPorEnviarFilter;
import java.util.List;

public interface BandejaEntradaDao {
    List<BandejaBean> listadoBandejaEntrada(BandejaEntradaFilter bandejaEntradaFilter);
    List<MisPendientesBean> listadoBandejaPendientes(BandejaPendientesFilter bandejaPendientesFilter);
    List<MisPendientesBean> listadoBandejaMisSolicitudes(BandejaMisSolicitudesFilter bandejaMisSolicitudesFilter);
    List<SolicitudesPorEnviarBean> listadoBandejaSolicitudesPorEnviar(BandejaSolicitudesPorEnviarFilter bandejaSolicitudesPorEnviarFilter);
}