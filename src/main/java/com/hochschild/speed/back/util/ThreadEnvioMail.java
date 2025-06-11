package com.hochschild.speed.back.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThreadEnvioMail implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(ThreadEnvioMail.class);

    private final String[] destinatarios;

    private final String titulo;

    private final String contenido;

    private final List<Map<String, String>> imagenesInline;

    private final List<File> archivosAdjuntos;

    public ThreadEnvioMail(String[] destinatarios, String titulo, String contenido) {
        // ApplicationContext
        // obj=WebApplicationContextUtils.getWebApplicationContext(contexto);
        // servicioMail=obj.getBean(ServicioMail.class);
        this.destinatarios = destinatarios;
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenesInline = new ArrayList<>();
        this.archivosAdjuntos = new ArrayList<>();
    }

    public ThreadEnvioMail(String[] destinatarios, String titulo, String contenido, List<Map<String, String>> imagenesInline) {
        // ApplicationContext
        // obj=WebApplicationContextUtils.getWebApplicationContext(contexto);
        // servicioMail=obj.getBean(ServicioMail.class);
        this.destinatarios = destinatarios;
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenesInline = imagenesInline;
        this.archivosAdjuntos = new ArrayList<>();
    }

    public ThreadEnvioMail(String[] destinatarios, String titulo, String contenido,List<Map<String, String>> imagenesInline,  List<File> archivosAdjuntos) {
        this.destinatarios = destinatarios;
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenesInline =  imagenesInline;
        this.archivosAdjuntos = archivosAdjuntos;
    }

    @Override
    public void run() {
        // servicioMail.sendMail(destinatarios,titulo,contenido);
        Mail mail = new Mail();
        String destinos = "";
        for (String destinatario : destinatarios) {
            mail.agregarDestinatario(destinatario);
            destinos += " " + destinatario + ",";
        }
        LOGGER.debug("Enviando notificacion por correo a " + destinos);
        mail.setAsunto(titulo);
        mail.setContenido(contenido);
        mail.setImagenesInline(imagenesInline);
        mail.setArchivosAdjuntos(archivosAdjuntos);

        try {
            if (mail.enviarCorreo()) {
                LOGGER.info("Notificaciones por correo enviadas a " + destinos);
            } else {
                LOGGER.warn("No se pudo enviar el correo");
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

}
