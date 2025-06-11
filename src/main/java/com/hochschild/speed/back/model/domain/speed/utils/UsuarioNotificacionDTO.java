package com.hochschild.speed.back.model.domain.speed.utils;
import java.io.Serializable; 

public class UsuarioNotificacionDTO implements Serializable {

    private Integer id;
    private String usuario;
    private Integer esGrupo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getEsGrupo() {
        return esGrupo;
    }

    public void setEsGrupo(Integer esGrupo) {
        this.esGrupo = esGrupo;
    }
    
}
