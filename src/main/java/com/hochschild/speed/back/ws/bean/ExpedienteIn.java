package com.hochschild.speed.back.ws.bean;

import java.util.List;

public class ExpedienteIn {
	
	private String asunto;
	
	private String observacion;
	
	private Integer idProceso;
	
	private ClienteIn cliente;
	
	private List<DocumentoIn> documentos;
	
	private String numeroExpedienteDicse;

	private Integer idUsuarioResponsable;
	
	private Integer idUsuarioCreacion;
	
	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Integer idProceso) {
		this.idProceso = idProceso;
	}

	public ClienteIn getCliente() {
		return cliente;
	}

	public void setCliente(ClienteIn cliente) {
		this.cliente = cliente;
	}

	public List<DocumentoIn> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<DocumentoIn> documentos) {
		this.documentos = documentos;
	}

	public String getNumeroExpedienteDicse() {
		return numeroExpedienteDicse;
	}

	public void setNumeroExpedienteDicse(String numeroExpedienteDicse) {
		this.numeroExpedienteDicse = numeroExpedienteDicse;
	}

	public Integer getIdUsuarioResponsable() {
		return idUsuarioResponsable;
	}

	public void setIdUsuarioResponsable(Integer idUsuarioResponsable) {
		this.idUsuarioResponsable = idUsuarioResponsable;
	}

	public Integer getIdUsuarioCreacion() {
		return idUsuarioCreacion;
	}

	public void setIdUsuarioCreacion(Integer idUsuarioCreacion) {
		this.idUsuarioCreacion = idUsuarioCreacion;
	}

	

}
