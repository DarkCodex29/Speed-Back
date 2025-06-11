package com.hochschild.speed.back.model.domain.speed.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class NotificacionContraparte {
    private String numeroSolicitud;
    private String nombreContratista;
    private String sumilla;
    private List<File> archivos;
    private List<String> destinatarios;
}
