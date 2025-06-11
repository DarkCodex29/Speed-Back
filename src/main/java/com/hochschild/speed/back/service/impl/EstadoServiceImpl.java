package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.EstadoBean;
import com.hochschild.speed.back.service.EstadoService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("EstadoService")
public class EstadoServiceImpl implements EstadoService {
    private static final Logger LOGGER = Logger.getLogger(EstadoServiceImpl.class.getName());


    @Override
    public List<EstadoBean> obtenerEstados() {

        List<EstadoBean> estadoLista = new ArrayList<>();

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_COMUNICACION
                        , "Comunicacion a Interesados"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_ELABORADO
                        , "Doc. Elaborado"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_EN_ELABORACION
                        , "En elaboración"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_EN_SOLICITUD
                        , "En Solicitud"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_ENVIADO_FIRMA
                        , "Enviado a Firma"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_ENVIADO_VISADO
                        , "Enviado a Visado"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_SOLICITUD_ENVIADA
                        , "Solicitud Enviada"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_VENCIDO
                        , "Vencido"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_VIGENTE
                        , "Vigente"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_VISADO
                        , "Visado"));

        return estadoLista;

    }

    @Override
    public List<EstadoBean> obtenerEstadosSeguimientoSolicitud() {

        List<EstadoBean> estadoLista = new ArrayList<>();

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_COMUNICACION
                        , "Comunicacion a Interesados"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_ELABORADO
                        , "Doc. Elaborado"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_EN_ELABORACION
                        , "En elaboración"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_EN_SOLICITUD
                        , "En Solicitud"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_ENVIADO_FIRMA
                        , "Enviado a Firma"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_ENVIADO_VISADO
                        , "Enviado a Visado"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_SOLICITUD_ENVIADA
                        , "Solicitud Enviada"));

        estadoLista.add(
                new EstadoBean(
                        Constantes.ESTADO_HC_VISADO
                        , "Visado"));

        return estadoLista;

    }

    @Override
    public String obtenerEstadoPorCodigoCharacter(Character codigo) {

        if (codigo.equals(Constantes.ESTADO_HC_EN_SOLICITUD)) {
            return "En Solicitud";
        }

        if (codigo.equals(Constantes.ESTADO_HC_SOLICITUD_ENVIADA)) {
            return "Solicitud Enviada";
        }

        if (codigo.equals(Constantes.ESTADO_HC_EN_ELABORACION)) {
            return "En Elaboración";
        }

        if (codigo.equals(Constantes.ESTADO_HC_ELABORADO)) {
            return "Doc. Elaborado";
        }

        if (codigo.equals(Constantes.ESTADO_HC_ENVIADO_VISADO)) {
            return "Enviado a Visado";
        }

        if (codigo.equals(Constantes.ESTADO_HC_VISADO)) {
            return "Visado";
        }

        if (codigo.equals(Constantes.ESTADO_HC_ENVIADO_FIRMA)) {
            return "Enviado a Firma";
        }

        if (codigo.equals(Constantes.ESTADO_HC_COMUNICACION)) {
            return "Comunicación a Interesados";
        }

        if (codigo.equals(Constantes.ESTADO_HC_VIGENTE)) {
            return "Vigente";
        }

        if (codigo.equals(Constantes.ESTADO_HC_VENCIDO)) {
            return "Vencido";
        }
        return "Estado sin mapear";
    }

    @Override
    public String obtenerEstadoPorCodigoString(String codigo) {

        if (codigo.equals(Constantes.ESTADO_HC_EN_SOLICITUD)) {
            return "En Solicitud";
        }

        if (codigo.equals(Constantes.ESTADO_HC_SOLICITUD_ENVIADA)) {
            return "Solicitud Enviada";
        }

        if (codigo.equals(Constantes.ESTADO_HC_EN_ELABORACION)) {
            return "En Elaboración";
        }

        if (codigo.equals(Constantes.ESTADO_HC_ELABORADO)) {
            return "Elaborado";
        }

        if (codigo.equals(Constantes.ESTADO_HC_ENVIADO_VISADO)) {
            return "Enviado Visado";
        }

        if (codigo.equals(Constantes.ESTADO_HC_VISADO)) {
            return "Visado";
        }

        if (codigo.equals(Constantes.ESTADO_HC_ENVIADO_FIRMA)) {
            return "Enviado Firma";
        }

        if (codigo.equals(Constantes.ESTADO_HC_COMUNICACION)) {
            return "Comunicación";
        }

        if (codigo.equals(Constantes.ESTADO_HC_VIGENTE)) {
            return "Vigente";
        }

        if (codigo.equals(Constantes.ESTADO_HC_VENCIDO)) {
            return "Vencido";
        }
        return "Estado sin mapear";
    }
}
