package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

@Data
public class ServicioResponse {
	
	private String resultado;

	private String codigoError;

	private String mensaje;
}