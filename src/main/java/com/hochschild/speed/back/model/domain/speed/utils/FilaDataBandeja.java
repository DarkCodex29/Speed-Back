package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.Date;
public class FilaDataBandeja {
	
	private Boolean leido;
	
	private Integer idTraza;

	private Integer idDocumentoLegal;

	private String documentoLegalNumero;
	
	private Date documentoLegalFechaSolicitud;
	
	private String cliente;
	
	private String nombreProceso;
	
	private String pathAlertGrid;
	
	private String documentoLegalEstado;
	
	private String documentoLegalSumilla;
	
	private String documentoLegalNombreCompania;
	
	private Date documentoLegalFechaBorrador;
	public Boolean getLeido() {
		return leido;
	}

	public void setLeido(Boolean leido) {
		this.leido = leido;
	}

	public String getDocumentoLegalNumero() {
		return documentoLegalNumero;
	}

	public void setDocumentoLegalNumero(String documentoLegalNumero) {
		this.documentoLegalNumero = documentoLegalNumero;
	}

	public Date getDocumentoLegalFechaSolicitud() {
		return documentoLegalFechaSolicitud;
	}

	public void setDocumentoLegalFechaSolicitud(Date documentoLegalFechaSolicitud) {
		this.documentoLegalFechaSolicitud = documentoLegalFechaSolicitud;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNombreProceso() {
		return nombreProceso;
	}

	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
	}

	public String getPathAlertGrid() {
		return pathAlertGrid;
	}

	public void setPathAlertGrid(String pathAlertGrid) {
		this.pathAlertGrid = pathAlertGrid;
	}

	public String getDocumentoLegalEstado() {
		return documentoLegalEstado;
	}

	public void setDocumentoLegalEstado(String documentoLegalEstado) {
		this.documentoLegalEstado = documentoLegalEstado;
	}

	public String getDocumentoLegalSumilla() {
		return documentoLegalSumilla;
	}

	public void setDocumentoLegalSumilla(String documentoLegalSumilla) {
		this.documentoLegalSumilla = documentoLegalSumilla;
	}

	public String getDocumentoLegalNombreCompania() {
		return documentoLegalNombreCompania;
	}

	public void setDocumentoLegalNombreCompania(String documentoLegalNombreCompania) {
		this.documentoLegalNombreCompania = documentoLegalNombreCompania;
	}

	public Date getDocumentoLegalFechaBorrador() {
		return documentoLegalFechaBorrador;
	}

	public void setDocumentoLegalFechaBorrador(Date documentoLegalFechaBorrador) {
		this.documentoLegalFechaBorrador = documentoLegalFechaBorrador;
	}

	public Integer getIdTraza() {
		return idTraza;
	}

	public void setIdTraza(Integer idTraza) {
		this.idTraza = idTraza;
	}

	public Integer getIdDocumentoLegal() {
		return idDocumentoLegal;
	}

	public void setIdDocumentoLegal(Integer idDocumentoLegal) {
		this.idDocumentoLegal = idDocumentoLegal;
	}
}