package com.hochschild.speed.back.ws.bean;

import java.util.Date;
import java.util.List;

public class DocumentosWs {
	
	private Integer id;
	
	private String numero;
	
	private String asunto;
	
	private String estado;

	private Date fechaDocumento;

	private Date fechaCreacion;

	private String tipoDocumento;
	
	private List<CamposAdicionalesWs> campos;
	
	private List<ArchivosWs> archivos;
	
	private String mensaje;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<CamposAdicionalesWs> getCampos() {
		return campos;
	}

	public void setCampos(List<CamposAdicionalesWs> campos) {
		this.campos = campos;
	}

	public List<ArchivosWs> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<ArchivosWs> archivos) {
		this.archivos = archivos;
	}
}
