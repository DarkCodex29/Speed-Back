package com.hochschild.speed.back.model.bean.elaborarDocumento;

import com.hochschild.speed.back.model.bean.mantenimiento.UsuarioBean;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import lombok.Data;

import java.util.List;

public @Data class DocumentoInfo {
    private Integer idDocumento;
    private List<UsuarioDestinatarioBean> destinatariosDefecto;
}
