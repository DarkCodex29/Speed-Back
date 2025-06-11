package com.hochschild.speed.back.model.domain.speed.utils;

public class CargoDTS {

	private String nroexpediente;
	private String tipodocumento;
	private String proceso;
	private String remitente;
	private String direccion;
	private String asunto;
	
	public String getNroexpediente() {
		return nroexpediente;
	}
	public void setNroexpediente(String nroexpediente) {
		this.nroexpediente = nroexpediente;
	}
	public String getTipodocumento() {
		return tipodocumento;
	}
	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}