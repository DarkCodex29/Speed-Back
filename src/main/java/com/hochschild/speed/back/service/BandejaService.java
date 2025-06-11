package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.filter.bandejaMisSolicitudes.BandejaMisSolicitudesFilter;
import com.hochschild.speed.back.model.filter.bandejaSolicitudesPorEnviar.BandejaSolicitudesPorEnviarFilter;
import com.hochschild.speed.back.model.filter.bandejaPendientes.BandejaPendientesFilter;
import com.hochschild.speed.back.model.filter.bandejaEntrada.BandejaEntradaFilter;
import com.hochschild.speed.back.model.bean.SolicitudesPorEnviarBean;
import com.hochschild.speed.back.model.bean.MisPendientesBean;
import com.hochschild.speed.back.model.bean.BandejaBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BandejaService {

    ResponseEntity<List<BandejaBean>> obtenerBandejaEntrada(BandejaEntradaFilter bandejaEntradaFilter);

    ResponseEntity<List<MisPendientesBean>> obtenerMisPendientes(BandejaPendientesFilter bandejaFilter);

    ResponseEntity<List<MisPendientesBean>> obtenerMisSolicitudes(BandejaMisSolicitudesFilter bandejaMisSolicitudesFilter);

    ResponseEntity<List<SolicitudesPorEnviarBean>> obtenerSolicitudesPorEnviar(BandejaSolicitudesPorEnviarFilter bandejaSolicitudesPorEnviarFilter);
}
