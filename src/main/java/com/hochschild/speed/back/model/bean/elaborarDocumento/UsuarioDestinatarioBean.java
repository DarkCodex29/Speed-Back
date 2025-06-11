package com.hochschild.speed.back.model.bean.elaborarDocumento;

import com.hochschild.speed.back.model.domain.speed.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDestinatarioBean {
    Usuario usuario;
    String ubicacion;
    String tipo;
}
