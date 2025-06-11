package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="TIPO_DOCUMENTO_POR_PROCESO")
public class TipoDocumentoPorProceso{
	
	@EmbeddedId
	private TipoDocumentoPorProcesoPK id;
	
	@Column(nullable=false)
	private int cantidad;

	public TipoDocumentoPorProceso() {

	}
}
