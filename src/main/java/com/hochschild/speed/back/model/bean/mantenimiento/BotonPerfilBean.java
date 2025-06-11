package com.hochschild.speed.back.model.bean.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.Perfil;
import lombok.Data;
import java.util.List;

public @Data class BotonPerfilBean {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String url;
    private String codigo;
    private String tipo;
    private String icono;
    private String parametro;
    private Boolean bloqueable;
    private Boolean bloqueableParalelo;
    private Boolean estado;
    private String eventoSubmit;
    private String eventoComplete;
    private Integer orden;
    private String iconoNuevo;
    private String botonClaseNuevo;
    private String urlNuevo;
    private List<Perfil> perfiles;
}