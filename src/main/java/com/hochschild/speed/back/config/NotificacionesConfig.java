package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:notificaciones.properties")
public @Data class NotificacionesConfig {

    @Value("${notificacion.registro.expediente}")
    private String registroExpediente;

    @Value("${notificacion.derivacion.expediente}")
    private String derivacionExpediente;

    @Value("${notificacion.documento.adjunto}")
    private String documentoAdjunto;

    @Value("${notificacion.alerta.expediente}")
    private String alertaExpediente;

    @Value("${notificacion.aceptar.solicitud}")
    private String aceptarSolicitud;

    @Value("${notificacion.enviar.solicitud}")
    private String enviarSolicitud;

    @Value("${notificacion.observar.solicitud}")
    private String observarSolicitud;

    @Value("${notificacion.registro.solicitud}")
    private String registroSolicitud;

    @Value("${notificacion.adjuntar.borrador}")
    private String adjuntarBorrador;

    @Value("${notificacion.adjuntar.primer.borrador}")
    private String adjuntarPrimerBorrador;

    @Value("${notificacion.adjuntar.siguiente.borrador}")
    private String adjuntarSiguienteBorrador;

    @Value("${notificacion.enviar.visado}")
    private String enviarVisado;

    @Value("${notificacion.observar.visado}")
    private String observarVisado;

    @Value("${notificacion.visado.aprobado}")
    private String visadoAprobado;

    @Value("${notificacion.envio.firma}")
    private String envioFirma;

    @Value("${notificacion.reenvio.firma.electronica}")
    private String reenvioFirmaElectronica;

    @Value("${notificacion.entrega.documento}")
    private String entregaDocumento;

    @Value("${notificacion.comunicacion.interesados}")
    private String comunicacionInteresados;

    @Value("${notificacion.registro.manual}")
    private String registroManual;

    @Value("${notificacion.alarma.vencimiento}")
    private String alarmaVencimiento;

    @Value("${notificacion.expediente.eliminado}")
    private String expedienteEliminado;

    @Value("${notificacion.cancelar.visado}")
    private String cancelarVisado;
}
