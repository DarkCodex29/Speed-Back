package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Data
@Embeddable
public class CampoPorDocumentoPK implements Serializable{

	private static final long serialVersionUID=6861115376318771970L;

	@ManyToOne
	@JoinColumn(name="id_documento",nullable=false)
	private Documento documento;

	@ManyToOne
	@JoinColumn(name="id_campo",nullable=false)
	private Campo campo;

	public CampoPorDocumentoPK(){
	}

	public CampoPorDocumentoPK(Documento documento,Campo campo){
		super();
		this.documento=documento;
		this.campo=campo;
	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((campo == null) ? 0 : campo.hashCode());
		result=prime * result + ((documento == null) ? 0 : documento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		CampoPorDocumentoPK other=(CampoPorDocumentoPK) obj;
		if(campo == null){
			if(other.campo != null)
				return false;
		}
		else if(!campo.equals(other.campo))
			return false;
		if(documento == null){
			if(other.documento != null)
				return false;
		}
		else if(!documento.equals(other.documento))
			return false;
		return true;
	}
}