package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="HC_USUARIO_POR_GRUPO")
public class HcUsuarioPorGrupo {

	@EmbeddedId
	private HcUsuarioPorGrupoPk id;

	public HcUsuarioPorGrupo() {
	}
}