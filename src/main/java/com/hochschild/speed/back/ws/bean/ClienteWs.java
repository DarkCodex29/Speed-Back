package com.hochschild.speed.back.ws.bean;

import java.util.Date;
import java.util.List;

public class ClienteWs {

	private Integer id;
	
	private String nombreCompleto;
	
	private String razonSocial;
	
	private String numeroIdentificacion;

	private String correo;
	
	private Date fechaCreacion;

	private String estado;

	private List<DireccionWs> direcciones;
	
	private String telefono;
	
	private String mensaje;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<DireccionWs> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<DireccionWs> direcciones) {
		this.direcciones = direcciones;
	}
	
	

	

}
