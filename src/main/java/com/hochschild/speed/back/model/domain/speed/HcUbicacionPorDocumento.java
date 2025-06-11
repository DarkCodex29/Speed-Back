package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "HC_UBICACION_POR_DOCUMENTO")
public class HcUbicacionPorDocumento {

	@EmbeddedId
	private HcUbicacionPorDocumentoPk id;

	public HcUbicacionPorDocumento() {

	}
}