package com.hochschild.speed.back.model.bean.registrarAdendaManual;

import lombok.Data;

import java.util.List;

public @Data class DocumentoLegalManualBean {

    private List<UsuarioNotificacionBean> usuarios;
    private Integer idExpediente;
    private Integer idResponsable;
}