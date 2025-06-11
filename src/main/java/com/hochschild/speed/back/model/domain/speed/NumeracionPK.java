package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class NumeracionPK implements Serializable{

	private static final long serialVersionUID=1L;

	@ManyToOne
	@JoinColumn(name="id_area",nullable=false,insertable=false,updatable=false)
	private Area area;

	@ManyToOne
	@JoinColumn(name="id_tipo_documento",nullable=false,insertable=false,updatable=false)
	private TipoDocumento tipoDocumento;

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((area == null) ? 0 : area.hashCode());
		result=prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
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
		NumeracionPK other=(NumeracionPK) obj;
		if(area == null){
			if(other.area != null)
				return false;
		}
		else if(!area.equals(other.area))
			return false;
		if(tipoDocumento == null){
			if(other.tipoDocumento != null)
				return false;
		}
		else if(!tipoDocumento.equals(other.tipoDocumento))
			return false;
		return true;
	}
}