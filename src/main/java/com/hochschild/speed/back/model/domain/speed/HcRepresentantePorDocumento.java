package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name="HC_REPRESENTANTE_POR_DOCUMENTO")
public class HcRepresentantePorDocumento {
	
	@EmbeddedId
	private HcRepresentantePorDocumentoPk id;

	public HcRepresentantePorDocumento() {

	}
}
