package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.Date;

public class FilaBandejaEntradaDTS {

	private String numero;
	private String compania;
	private String contraparte;
	private String sumilla;
	private Character estado;
	private Date fechaSolicitud;
	private Date fechaBorrador;
	private String strEstado;
	private String strFechaSolicitud;
	private String strFechaBorrador;
	private String proceso;
	
	public FilaBandejaEntradaDTS(){
		
	}
	
	public FilaBandejaEntradaDTS(String numero, String compania, String contraparte, String sumilla, Character estado,
			Date fechaSolicitud, Date fechaBorrador, String proceso) {
		this.numero = numero;
		this.compania = compania;
		this.contraparte = contraparte;
		this.sumilla = sumilla;
		this.estado = estado;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaBorrador = fechaBorrador;
		this.proceso = proceso;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCompania() {
		return compania;
	}
	public void setCompania(String compania) {
		this.compania = compania;
	}
	public String getContraparte() {
		return contraparte;
	}
	public void setContraparte(String contraparte) {
		this.contraparte = contraparte;
	}
	public String getSumilla() {
		return sumilla;
	}
	public void setSumilla(String sumilla) {
		this.sumilla = sumilla;
	}
	public Character getEstado() {
		return estado;
	}
	public void setEstado(Character estado) {
		this.estado = estado;
	}
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public Date getFechaBorrador() {
		return fechaBorrador;
	}
	public void setFechaBorrador(Date fechaBorrador) {
		this.fechaBorrador = fechaBorrador;
	}
	public String getStrEstado() {
		return strEstado;
	}
	public void setStrEstado(String strEstado) {
		this.strEstado = strEstado;
	}
	public String getStrFechaSolicitud() {
		return strFechaSolicitud;
	}
	public void setStrFechaSolicitud(String strFechaSolicitud) {
		this.strFechaSolicitud = strFechaSolicitud;
	}
	public String getStrFechaBorrador() {
		return strFechaBorrador;
	}
	public void setStrFechaBorrador(String strFechaBorrador) {
		this.strFechaBorrador = strFechaBorrador;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
}
