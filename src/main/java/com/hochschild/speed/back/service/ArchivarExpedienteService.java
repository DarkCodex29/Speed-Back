package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.bandejaEntrada.ArchivarBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.DatosArchivarBean;
import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.domain.speed.Usuario;

import java.util.List;

public interface ArchivarExpedienteService {

    DatosArchivarBean botonDatosArchivar(Integer idExpediente, Boolean eliminarSolicitud);

    Integer archivarExpediente(ArchivarBean archivarBean, Integer idUsuario);

    Integer archivarExpediente(Expediente expediente, Usuario remitente, String observacion, String accion);

    Integer archivarExpedienteEnSolicitud(Expediente expediente, Usuario remitente, String observacion, String accion);

    Integer archivarExpedienteWS(Integer idExpediente, String numeroExpediente, String remitente, String observacion);

    Expediente getExpediente(Integer idExpediente);

    List<Traza> obtenerTrazas(Integer[] idTrazas);

    List<String> expedienteDerivacionSimple(List<Traza> trazas);

    List<String> expedienteDerivadoRol(List<Traza> trazas);

    List<String> expedienteDerivacionMultiple(List<Traza> trazas);

    List<String> expedienteProcesoWorkflow(List<Traza> trazas);

    List<String> expedienteProcesoIntalio(List<Traza> trazas);

    Expediente obtenerExpedientePorTraza(Integer idTraza);
}
