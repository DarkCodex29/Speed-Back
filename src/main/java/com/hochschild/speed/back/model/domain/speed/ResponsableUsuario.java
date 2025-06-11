package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="RESPONSABLE_USUARIO")
public class ResponsableUsuario {

	@EmbeddedId
	private ResponsableUsuarioPK id;

	public ResponsableUsuario() {
	}
}