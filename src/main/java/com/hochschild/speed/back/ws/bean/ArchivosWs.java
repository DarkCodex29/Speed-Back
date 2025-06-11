package com.hochschild.speed.back.ws.bean;

import java.util.Date;

public class ArchivosWs {
	
	private Integer id;
	
	private String nombre;
	
	private Date fechaCreacion;
	
	private String estado;

	private String rutaAlfresco;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getRutaAlfresco() {
		return rutaAlfresco;
	}

	public void setRutaAlfresco(String rutaAlfresco) {
		this.rutaAlfresco = rutaAlfresco;
	}

 

}
