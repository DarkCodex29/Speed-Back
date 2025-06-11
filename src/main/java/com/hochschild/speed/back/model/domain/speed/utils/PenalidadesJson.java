package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;

public class PenalidadesJson {
	
	private List<PenalidadJson> penalidades;
	
	private Integer idExpediente;

	public List<PenalidadJson> getPenalidades() {
		return penalidades;
	}

	public void setPenalidades(List<PenalidadJson> penalidades) {
		this.penalidades = penalidades;
	}

	public Integer getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}
}