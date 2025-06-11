package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name="USUARIO_POR_ROL")
public class UsuarioPorRol {

	@EmbeddedId
	private UsuarioPorRolPk id;

	public UsuarioPorRol() {

	}
}
