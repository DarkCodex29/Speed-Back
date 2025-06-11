package com.hochschild.speed.back.model.auth;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hochschild.speed.back.model.domain.speed.Busqueda;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import lombok.Data;

/**
 *
 * @author HEEDCOM S.A.C.
 * @since 27/12/2019
 */
public @Data class User {

    private String token;
    @JsonIgnore
    private Integer idUsuario;
    @JsonIgnore
    private Integer idPerfil;
    private String usuario;
    @JsonIgnore
    private String password;
    private String nombre;
    private String correo;
    private String area;
    private String puesto;
    private List<String> rolesUsuario = null;
    private List<Opcion> listaOpciones = null;
    private List<Busqueda> busquedasGuardada = null;

    public User(Usuario usuario){
        this.idUsuario=usuario.getId();
        this.usuario=usuario.getUsuario();
        this.nombre=usuario.getNombreCompleto();
        this.correo=usuario.getCorreo();
        this.area="";
        this.puesto="";
    }

    public User(){
    }
}
