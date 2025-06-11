package com.hochschild.speed.back.ws.bean;

import java.util.List;

public class DocumentoIn{

	private Integer tipoDocumento;

	private String fechaDocumento;

	private String asunto;

	private String numeroDocumento;

	private boolean numerarDocumento;

	private Integer idAutor;

	private List<CamposIn> campos;

	public Integer getTipoDocumento(){
		return tipoDocumento;
	}

	public void setTipoDocumento(Integer tipoDocumento){
		this.tipoDocumento=tipoDocumento;
	}

	public String getFechaDocumento(){
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento){
		this.fechaDocumento=fechaDocumento;
	}

	public String getAsunto(){
		return asunto;
	}

	public void setAsunto(String asunto){
		this.asunto=asunto;
	}

	public List<CamposIn> getCampos(){
		return campos;
	}

	public void setCampos(List<CamposIn> campos){
		this.campos=campos;
	}

	public String getNumeroDocumento(){
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento){
		this.numeroDocumento=numeroDocumento;
	}

	public boolean isNumerarDocumento(){
		return numerarDocumento;
	}

	public void setNumerarDocumento(boolean numerarDocumento){
		this.numerarDocumento=numerarDocumento;
	}

	public Integer getIdAutor(){
		return idAutor;
	}

	public void setIdAutor(Integer idAutor){
		this.idAutor=idAutor;
	}

}
