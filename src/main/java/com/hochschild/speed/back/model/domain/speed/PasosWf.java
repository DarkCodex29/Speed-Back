package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

@Data
public class PasosWf {
	
	private String nombre;

	private String usuarioDestinatario;
	
	private String rolDestinatario;
	
	private Integer orden;
}
