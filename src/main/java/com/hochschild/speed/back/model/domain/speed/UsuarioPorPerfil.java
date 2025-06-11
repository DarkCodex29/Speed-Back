package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name="USUARIO_POR_PERFIL")
public class UsuarioPorPerfil {

	@EmbeddedId
	private UsuarioPorPerfilPk id;

	public UsuarioPorPerfil() {
		
	}
}
