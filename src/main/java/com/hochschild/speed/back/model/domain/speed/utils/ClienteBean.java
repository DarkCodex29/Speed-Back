package com.hochschild.speed.back.model.domain.speed.utils;

/**
 *
 * @author ANYI RODRIGUEZ
 */
public class ClienteBean {

    private int id;
    private String nombre;
    private String apelidPaterno;
    private int tipo;
    private String apellidoMaterno;
    private String correo;
    private String numeroIdentificacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApelidPaterno() {
        return apelidPaterno;
    }

    public void setApelidPaterno(String apelidPaterno) {
        this.apelidPaterno = apelidPaterno;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }
}