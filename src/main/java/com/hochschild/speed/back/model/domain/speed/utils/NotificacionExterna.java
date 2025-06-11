package com.hochschild.speed.back.model.domain.speed.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class NotificacionExterna {
    private String numeroSolicitud;
    private String tipoProceso;
    private String sumilla;
    private String compania;
    private String contraparte;
    private String razonSocialContraparte;
    private String descripcionContrato;
    private List<File> archivos;
    private List<String> destinatarios;
}
