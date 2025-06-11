package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name = "USUARIO_ENVIO_EXPEDIENTE")
public class UsuarioEnvioPendiente{
	
	@EmbeddedId
	private UsuarioEnvioPendientePK id;
	
	@Column(nullable=false)
	private Character tipo;

	public UsuarioEnvioPendiente() {

	}
}