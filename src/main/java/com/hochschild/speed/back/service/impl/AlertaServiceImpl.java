package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.AlertaRepository;
import com.hochschild.speed.back.repository.speed.FeriadoRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.service.AlertaService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("AlertaService")
public class AlertaServiceImpl implements AlertaService {
    private final ParametroRepository parametroRepository;
    private final AlertaRepository alertaRepository;
    private final FeriadoRepository feriadoRepository;

    private static final Logger LOGGER = Logger.getLogger(AlertaServiceImpl.class.getName());

    public AlertaServiceImpl(ParametroRepository parametroRepository,
                             AlertaRepository alertaRepository,
                             FeriadoRepository feriadoRepository) {
        this.parametroRepository = parametroRepository;
        this.alertaRepository = alertaRepository;
        this.feriadoRepository = feriadoRepository;
    }

    @Override
    public Alerta obtenerAlerta(Expediente expediente, Traza traza, Integer idGrid) {

        LOGGER.info("Obteniendo alerta para la Traza " + traza.getId());

        Date fechaInicioPlazo = null;
        Date fechaFinPlazo = null;
        Date fechaHoy = new Date();
        Proceso proceso = expediente.getProceso();

        //obtener plazos de acuerdo a cada estado
        Character estadoDL = expediente.getDocumentoLegal().getEstado();
        List<Parametro> parametroPlazos = parametroRepository.obtenerPorTipo(Constantes.TIPO_PLAZO_GENERAL.concat(estadoDL.toString()));
        Parametro parametroPlazo = parametroPlazos != null ? parametroPlazos.get(0) : null;

        if (parametroPlazo != null) {
            try {
                Integer plazo = Integer.parseInt(parametroPlazo.getValor());
                if (plazo != null && plazo != 0) {
                    fechaInicioPlazo = traza.getFechaCreacion();
                    fechaFinPlazo = DateUtil.sumarDiasUtiles(traza.getFechaCreacion(), plazo, expediente.getCreador().getArea().getSede(), feriadoRepository);
                }
            } catch (NullPointerException e) {
                LOGGER.info("El creador del expediente no tenía área asignada. Se calculará el plazo sin feriados");
                fechaFinPlazo = DateUtil.sumarDiasUtilesSinFeriados(expediente.getFechaCreacion(), proceso.getPlazo());
            }

        } else {
            if (proceso.getTipoProceso().getAlerta().equals(Constantes.TIPO_PROCESO_ALERTA_PROCESO)) {
                if (proceso.getPlazo() != null && proceso.getPlazo() != 0) {
                    fechaInicioPlazo = expediente.getFechaCreacion();
                    try {
                        fechaFinPlazo = DateUtil.sumarDiasUtiles(expediente.getFechaCreacion(), proceso.getPlazo(), expediente.getCreador().getArea().getSede(), feriadoRepository);
                    } catch (NullPointerException e) {
                        LOGGER.info("El creador del expediente no tenía área asignada. Se calculará el plazo sin feriados");
                        fechaFinPlazo = DateUtil.sumarDiasUtilesSinFeriados(expediente.getFechaCreacion(), proceso.getPlazo());
                    }
                }
            } else {
                fechaInicioPlazo = traza.getFechaCreacion();
                fechaFinPlazo = traza.getFechaLimite();
            }
        }
        List<Alerta> alertas = alertaRepository.obtenerPorGrid(idGrid);

        if (fechaInicioPlazo != null && fechaFinPlazo != null) {

            long plazoTotal = DateUtil.diferenciaFechasHoras(fechaInicioPlazo, fechaFinPlazo);
            long tiempoTranscurrido = DateUtil.diferenciaFechasHoras(fechaInicioPlazo, fechaHoy);

            if (plazoTotal == 0l) {
                plazoTotal = 1l;
            }

            double porcentajeTiempoTranscurrido = tiempoTranscurrido * 100 / plazoTotal;

            TipoAlerta tipoAlertaElegida = null;
            Alerta alertaElegida = null;
            for (Alerta alerta : alertas) {
                if (porcentajeTiempoTranscurrido >= (double) alerta.getTipoAlerta().getPorcentaje()) {
                    if (tipoAlertaElegida != null && tipoAlertaElegida.getPorcentaje() < alerta.getTipoAlerta().getPorcentaje()) {
                        tipoAlertaElegida = alerta.getTipoAlerta();
                        alertaElegida = alerta;
                    } else if (tipoAlertaElegida == null) {
                        tipoAlertaElegida = alerta.getTipoAlerta();
                        alertaElegida = alerta;
                    }
                }
            }
            if (alertaElegida != null) {
                return alertaElegida;
            }
        }
        for (Alerta alerta : alertas) {
            if (alerta.getTipoAlerta().isDefecto()) {
                return alerta;
            }
        }
        return null;
    }
}