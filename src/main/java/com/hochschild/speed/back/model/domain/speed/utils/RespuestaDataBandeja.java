package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;

public class RespuestaDataBandeja {
	
	private List<FilaDataBandeja> filas;
	
	private String mensaje;

	public List<FilaDataBandeja> getFilas() {
		return filas;
	}

	public void setFilas(List<FilaDataBandeja> filas) {
		this.filas = filas;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}