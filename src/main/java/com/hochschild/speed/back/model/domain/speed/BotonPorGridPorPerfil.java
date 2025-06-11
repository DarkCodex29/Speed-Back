package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "BOTON_POR_GRID_POR_PERFIL")
public class BotonPorGridPorPerfil{
	
	@EmbeddedId
	private BotonPorGridPorPerfilPK id;

	public BotonPorGridPorPerfil() {
	}

	public BotonPorGridPorPerfil(BotonPorGridPorPerfilPK id) {
		super();
		this.id = id;
	}
}