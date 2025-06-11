package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.NotificacionesConfig;
import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.dao.UsuarioDao;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.EnvioAlarmaService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EnvioAlarmaServiceImpl implements EnvioAlarmaService {
    Logger log = LoggerFactory.getLogger(EnvioAlarmaServiceImpl.class);

    private final HcAlarmaRepository alarmaRepository;
    private final EnviarNotificacionService enviarNotificacionService;
    private final HcDestinatarioAlarmaRepository destinatarioAlarmaRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcGrupoRepository hcGrupoRepository;
    private final UsuarioRepository usuarioRepository;

    private final NotificacionesConfig notificacionesConfig;

    private final HcDocumentoLegalDao hcDocumentoLegalDao;

    private final UsuarioDao usuarioDao;

    @Autowired
    public EnvioAlarmaServiceImpl(HcAlarmaRepository alarmaRepository, EnviarNotificacionService enviarNotificacionService, HcDestinatarioAlarmaRepository destinatarioAlarmaRepository, TipoNotificacionRepository tipoNotificacionRepository, HcDocumentoLegalRepository hcDocumentoLegalRepository, HcGrupoRepository hcGrupoRepository, UsuarioRepository usuarioRepository, NotificacionesConfig notificacionesConfig, HcDocumentoLegalDao hcDocumentoLegalDao, UsuarioDao usuarioDao) {
        this.alarmaRepository = alarmaRepository;
        this.enviarNotificacionService = enviarNotificacionService;
        this.destinatarioAlarmaRepository = destinatarioAlarmaRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcGrupoRepository = hcGrupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionesConfig = notificacionesConfig;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    @Transactional
    public void envioAlarmas() {
        log.info("=============================ENVIO ALARMAS =================================\n\n\n");

        log.info("EJECUTANDO PROCESO ALARMAS A LAS: " + DateUtil.convertDateToString(new Date(), DateUtil.FORMAT_DATE_HOUR));
        log.info("Sincronizando correos");


        Calendar hoy = normalizarFecha(Calendar.getInstance());

        //Sincronizacion de grupos de usuarios
        List<HcGrupo> grupos = hcGrupoRepository.obtenerActivos();

        if (grupos != null && !grupos.isEmpty()) {
            List<Integer> usuariosSincronizados = new ArrayList<>();
            for (HcGrupo grupo : grupos) {
                List<Usuario> usuariosGrupo = hcGrupoRepository.obtenerUsuariosGrupo(grupo.getId());

                if (usuariosGrupo != null && !usuariosGrupo.isEmpty()) {
                    boolean vacio = true;
                    for (Usuario usuario : usuariosGrupo) {
                        if (!usuariosSincronizados.contains(usuario.getId())) {
                            log.debug("Sincronizando Usuario " + usuario);
                            String correo = usuarioDao.obtenerCorreoUsuarioSCA(usuario.getUsuario());
                            usuario.setCorreo(correo);
                            usuarioRepository.save(usuario);

                            usuariosSincronizados.add(usuario.getId());
                        }
                    }

                    if (vacio) {
                        //TODO el grupo esta vacio, deberia enviarse a un correo estandar
                    }
                }
            }
        }

        log.info("Enviando alarmas");
        //Enviando alarmas
        List<HcAlarma> alarmas = alarmaRepository.obtenerAlarmasActivas();

        if (alarmas != null && !alarmas.isEmpty()) {
            for (HcAlarma alarma : alarmas) {
                Calendar vencimiento = Calendar.getInstance();
                vencimiento.setTime(alarma.getFechaAlarma());
                Boolean isAnual;
                if (alarma.getAnual() == null) {
                    isAnual = false;
                } else {
                    isAnual = alarma.getAnual();
                }
                if (fechasIguales(hoy, vencimiento)) {
                    //Se envia la ultima alarma, luego se desactiva
                    enviarAlarma(alarma);

                    if (isAnual) {
                        Calendar calCon = Calendar.getInstance();
                        if (alarma.getDocumentoLegal().getContrato() != null) {
                            if (alarma.getDocumentoLegal().getContrato().getIndefinido()) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(vencimiento.getTime());
                                cal.add(Calendar.YEAR, 1);
                                alarma.setFechaAlarma(cal.getTime());
                            } else if (alarma.getDocumentoLegal().getContrato().getFechaFin() != null) {
                                calCon.setTime(alarma.getDocumentoLegal().getContrato().getFechaFin());
                                if (calCon.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)) {
                                    alarma.setEstado(Constantes.ESTADO_INACTIVO);
                                } else {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(vencimiento.getTime());
                                    cal.add(Calendar.YEAR, 1);
                                    alarma.setFechaAlarma(cal.getTime());
                                }
                            }
                        } else if (alarma.getDocumentoLegal().getAdenda().getContrato() != null) {

                            if (alarma.getDocumentoLegal().getAdenda().getContrato().getIndefinido()) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(vencimiento.getTime());
                                cal.add(Calendar.YEAR, 1);
                                alarma.setFechaAlarma(cal.getTime());
                            } else if (alarma.getDocumentoLegal().getAdenda().getContrato().getFechaFin() != null) {
                                calCon.setTime(alarma.getDocumentoLegal().getAdenda().getContrato().getFechaFin());
                                if (calCon.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)) {
                                    alarma.setEstado(Constantes.ESTADO_INACTIVO);
                                } else {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(vencimiento.getTime());
                                    cal.add(Calendar.YEAR, 1);
                                    alarma.setFechaAlarma(cal.getTime());
                                }
                            }
                        }

                    } else {
                        alarma.setEstado(Constantes.ESTADO_INACTIVO);
                    }

                    alarmaRepository.save(alarma);

                } else {
                    //Se valida si esta en el intervalo
                    boolean envioAlarma = false;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(vencimiento.getTime());
                    cal.add(Calendar.DAY_OF_MONTH, (-1) * alarma.getDias_activacion());
                    log.info("Fecha de alarma en el flujo para validar si el doc " + alarma.getDocumentoLegal().getNumero() + " esta en el intervalo " + cal);

                    do {
                        if (fechasIguales(hoy, cal)) {
                            envioAlarma = true;
                        }
                        cal.add(Calendar.DAY_OF_MONTH, alarma.getDias_intervalo());
                    } while (!envioAlarma && cal.before(hoy));
                    log.info("El envio de alarma es: " + envioAlarma + " entonces, la alarma " + alarma.getDocumentoLegal().getNumero());
                    if (envioAlarma) {
                        enviarAlarma(alarma);
                    }
                }
            }
        }

        //Cambiando estado a contratos que pasan a vencido
        List<HcDocumentoLegal> contratosVencidos = hcDocumentoLegalDao.obtenerVencimientoHoy();

        if (contratosVencidos != null && !contratosVencidos.isEmpty()) {
            for (HcDocumentoLegal documento : contratosVencidos) {
                if (documento.getEstado().equals(Constantes.ESTADO_HC_VIGENTE)) {
                    log.info("El documento [" + documento.getNumero() + "] ha vencido");
                    documento.setEstado(Constantes.ESTADO_HC_VENCIDO);
                    hcDocumentoLegalRepository.save(documento);
                }
            }
        }
    }

    private void enviarAlarma(HcAlarma alarma) {
        List<Usuario> destinatarios = destinatarioAlarmaRepository.obtenerUsuariosDestinatariosAlarmaPart1(alarma.getId());
        destinatarios.addAll(destinatarioAlarmaRepository.obtenerUsuariosDestinatariosAlarmaPart2(alarma.getId()));
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.obtenerPorCodigo(notificacionesConfig.getAlarmaVencimiento());
        enviarNotificacionService.registrarNotificacionAlarma(destinatarios, tipoNotificacion, alarma.getDocumentoLegal(), alarma.getMensaje());
    }

    private Calendar normalizarFecha(Calendar fecha) {
        fecha.set(Calendar.HOUR_OF_DAY, 0);
        fecha.set(Calendar.MINUTE, 0);
        fecha.set(Calendar.SECOND, 0);
        fecha.set(Calendar.MILLISECOND, 0);
        return fecha;
    }

    private boolean fechasIguales(Calendar fechaA, Calendar fechaB) {
        return (fechaA.get(Calendar.DAY_OF_MONTH) == fechaB.get(Calendar.DAY_OF_MONTH) &&
                fechaA.get(Calendar.MONTH) == fechaB.get(Calendar.MONTH) &&
                fechaA.get(Calendar.YEAR) == fechaB.get(Calendar.YEAR));
    }


}
