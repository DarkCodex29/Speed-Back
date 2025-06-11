package com.hochschild.speed.back.ws.bean;

import java.util.Date;

public class ExpedienteWs {
	
	private String numero;
	
	private String estado;
	
	private String asunto;
	
	private Date fechaCreacion;
	
	private ProcesoWs proceso;
	
	private UsuarioWS creador;
	
	private String tipoCliente;
	
	private String numeroDocumentoIdentidad;
	
	private String mensaje;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public ProcesoWs getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoWs proceso) {
		this.proceso = proceso;
	}

	public UsuarioWS getCreador() {
		return creador;
	}

	public void setCreador(UsuarioWS creador) {
		this.creador = creador;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}



	public String getNumeroDocumentoIdentidad() {
		return numeroDocumentoIdentidad;
	}

	public void setNumeroDocumentoIdentidad(String numeroDocumentoIdentidad) {
		this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
