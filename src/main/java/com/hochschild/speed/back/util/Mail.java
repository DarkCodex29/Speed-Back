package com.hochschild.speed.back.util;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public @Data class Mail {

    private Logger LOGGER = LoggerFactory.getLogger(Mail.class);

    private ResourceBundle resourceBundle;

    private String server;

    private String port;

    private InternetAddress from;

    private String asunto;

    private List<InternetAddress> destinatarios;

    private List<InternetAddress> copias;

    private String contenido;

    private String tipoContenido;

    private List<String> adjuntos;

    private List<Map<String, String>> imagenesInline;

    private List<File> archivosAdjuntos;

    public Mail() {
        resourceBundle = ResourceBundle.getBundle("mail");
        server = resourceBundle.getString("mail.host");
        port = resourceBundle.getString("mail.port");
        try {
            from = new InternetAddress(resourceBundle.getString("mail.user"));
        } catch (AddressException e) {
            from = null;
        }
        tipoContenido = "text/html; charset=UTF-8";
        destinatarios = new ArrayList<>();
        copias = new ArrayList<>();
        adjuntos = new ArrayList<>();
        imagenesInline = new ArrayList<>();
        archivosAdjuntos = new ArrayList<>();
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void agregarDestinatario(String correo) {
        try {
            InternetAddress ia = new InternetAddress(correo);
            destinatarios.add(ia);
        } catch (AddressException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public void agregarCopia(String correo) {
        try {
            InternetAddress ia = new InternetAddress(correo);
            copias.add(ia);
        } catch (AddressException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public void agregarAdjunto(String archivo) {
        adjuntos.add(archivo);
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean enviarCorreo() throws IOException {
        if (from != null && !destinatarios.isEmpty()) {
            Properties p = new Properties();
            p.put("mail.smtp.port", port);
            p.put("mail.smtp.host", server);
            //log.debug("PUERTO!!!!!!!!!!!!!! " + port);
            p.put("mail.smtp.starttls.enable", resourceBundle.getString("mail.starttls.enable"));
            p.put("mail.smtps.auth", resourceBundle.getString("mail.auth"));
            Session sesion = Session.getInstance(p);
            sesion.setDebug(Boolean. parseBoolean(resourceBundle.getString("mail.debug")));
            MimeMessage msg = new MimeMessage(sesion);
            try {

                msg.setFrom(from);
                for (InternetAddress ia : destinatarios) {
                    msg.addRecipient(Message.RecipientType.TO, ia);
                }
                for (InternetAddress ia : copias) {
                    msg.addRecipient(Message.RecipientType.CC, ia);
                }
                Multipart mp = new MimeMultipart("related");
                MimeBodyPart texto = new MimeBodyPart();
                texto.setContent(contenido, tipoContenido);
                mp.addBodyPart(texto);
                for (String adjunto : adjuntos) {
                    FileDataSource fds = new FileDataSource(adjunto);
                    MimeBodyPart archivo = new MimeBodyPart();
                    archivo.setDataHandler(new DataHandler(fds));
                    archivo.setFileName(fds.getName());
                    mp.addBodyPart(archivo);
                }

                for (File archivo : archivosAdjuntos) {
                    FileDataSource fds = new FileDataSource(archivo);
                    MimeBodyPart archivoAdjunto = new MimeBodyPart();
                    archivoAdjunto.setDataHandler(new DataHandler(fds));
                    archivoAdjunto.setFileName(fds.getName());
                    mp.addBodyPart(archivoAdjunto);
                }

                for (Map<String, String> imagen : imagenesInline) {
                    LOGGER.error("path: "+imagen.get("path"));
                    LOGGER.error("cid: "+imagen.get("cid"));
                    MimeBodyPart imagePart = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(imagen.get("path"));
                    imagePart.setDataHandler(new DataHandler(fds));
                    imagePart.setContentID("<" + imagen.get("cid") + ">");
                    imagePart.setDisposition(MimeBodyPart.INLINE);
                    mp.addBodyPart(imagePart);
                }
                msg.setContent(mp);
                msg.setSubject(asunto, "utf-8");
                msg.saveChanges();
                Transport t = sesion.getTransport(resourceBundle.getString("mail.transport"));
                t.connect(server, Integer.parseInt("" + port), resourceBundle.getString("mail.user"), resourceBundle.getString("mail.pass"));
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                LOGGER.info("LLEGO ENVIO DE CORREO");

                t.sendMessage(msg, msg.getAllRecipients());
                t.close();
            } catch (MessagingException e) {
                LOGGER.error("Error enviando correo", e);
                return false;
            }
            return true;
        }
        return false;
    }

    /*
	 * private class SMTPAuthenticator extends Authenticator{ public
	 * PasswordAuthentication getPasswordAuthentication(){ String
	 * username=Config.getPropiedad("mail.user"); String
	 * password=Config.getPropiedad("mail.pass"); return new
	 * PasswordAuthentication(username,password); } }
     */
}
