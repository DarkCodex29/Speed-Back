package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class HcRepresentantePorDocumentoPk implements Serializable{

	private static final long serialVersionUID=-1133740396497094500L;
	
	@ManyToOne
	@JoinColumn(name="id_documento_legal", nullable=false, updatable=false)
	private HcDocumentoLegal documentoLegal;
	
	@ManyToOne
	@JoinColumn(name="id_representate", nullable=false)
	private Cliente cliente;
}