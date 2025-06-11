package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="RESPONSABLE_ROL")
public class ResponsableRol {

	@EmbeddedId
	private ResponsableRolPK id;

	public ResponsableRol() {
	}
}