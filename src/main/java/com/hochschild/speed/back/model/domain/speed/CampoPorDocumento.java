package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name="CAMPO_POR_DOCUMENTO")
public class CampoPorDocumento{

	@EmbeddedId
	private CampoPorDocumentoPK id;
	
	@Column(length=1024)
	private String valor;

	public CampoPorDocumento() {

	}

	public CampoPorDocumento(Campo campo,Documento documento){
		id=new CampoPorDocumentoPK();
		id.setCampo(campo);
		id.setDocumento(documento);
	}
}
