package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="HC_REPRESENTANTE_POR_CONTRAPARTE")
public class HcRepresentantePorContraparte {

	@EmbeddedId
	private HcRepresentantePorContrapartePK id;

	public HcRepresentantePorContraparte() {

	}
}