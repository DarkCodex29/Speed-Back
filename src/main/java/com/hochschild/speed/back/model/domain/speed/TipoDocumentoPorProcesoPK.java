package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class TipoDocumentoPorProcesoPK implements Serializable{

	private static final long serialVersionUID=387745951121735059L;
	
	@Column(name="id_tipo_documento",nullable=false)
	private Integer idTipoDocumento;

	@Column(name="id_proceso",nullable=false)
	private Integer idProceso;

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((idProceso == null) ? 0 : idProceso.hashCode());
		result=prime * result + ((idTipoDocumento == null) ? 0 : idTipoDocumento.hashCode());
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
		TipoDocumentoPorProcesoPK other=(TipoDocumentoPorProcesoPK) obj;
		if(idProceso == null){
			if(other.idProceso != null)
				return false;
		}
		else if(!idProceso.equals(other.idProceso))
			return false;
		if(idTipoDocumento == null){
			if(other.idTipoDocumento != null)
				return false;
		}
		else if(!idTipoDocumento.equals(other.idTipoDocumento))
			return false;
		return true;
	}
}