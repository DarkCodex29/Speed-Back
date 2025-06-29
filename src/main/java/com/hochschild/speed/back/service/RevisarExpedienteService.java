package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.*;
import java.util.List;
import java.util.Date;

public interface RevisarExpedienteService {
    Reemplazo buscarReemplazo(Integer idReemplazante, Proceso proceso);

    Traza obtenerUltimaTraza(Integer idExpediente, Integer idUsuario);

    UsuarioPorTraza obtenerUsuarioPorTraza(Integer idTraza, Integer idUsuario);

    Boolean usuarioPuedeVerExpedienteConfidencial(Usuario usuario, Expediente expediente);

    List<Documento> obtenerDocumentos(Expediente expediente);

    List<CampoPorDocumento> obtenerCamposPorDocumento(Documento documento);

    Date obtenerFechaLimite(Expediente expediente, Integer plazo);

    void bloquearExpediente(UsuarioPorTraza ut);

    void cambiarEstadoLeido(UsuarioPorTraza ut);

    EnvioPendiente obtenerEnvioPendiente(Usuario remitente, Expediente expediente);

    Boolean usuarioEsAbogado(Usuario usuario);

    List<Boton> obtenerBotones(Perfil perfil);

    List<Boton> obtenerBotones(Expediente expediente, Perfil perfilSesion, Character paraEstado);

    List<Boton> obtenerBotones(Expediente expediente, Perfil perfil, Boolean responsable, Character paraEstado);



    List<Boton> obtenerBotonesExceptoParametro(Perfil perfil, Expediente expediente, Usuario usuario, Boolean responsable, Character paraEstado, String parametro);

    List<Boton> obtenerBotonesPorRolExceptoParametro(Perfil perfil, Expediente expediente, Usuario usuario, Boolean responsable, Character paraEstado, String parametro);

    List<Boton> obtenerBotonesMostrarCargo(Perfil perfil);
    Traza getPrimeraTraza(Expediente expediente);
    List<Rol> buscarRolesPorUsuario(Integer idUsuario);
    List<Proceso> listarProcesos();
    List<Usuario> devolverUsuariosResponsablesPorProceso(Integer idProceso);
    Boolean devolverFlagCliente(Integer idProceso);

    /**
     * Nuevo método que implementa la matriz Estados vs Botones vs Roles
     * según las especificaciones del cliente
     */
    List<Boton> obtenerBotonesMatrizEstadosRoles(Expediente expediente, Usuario usuario, Perfil perfil, Character estadoDocumentoLegal);
}