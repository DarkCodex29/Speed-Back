package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name="RECURSO_POR_PERFIL")
public class RecursoPorPerfil{

	@EmbeddedId
	private RecursoPorPerfilPK id;

	@ManyToOne
	@JoinColumn(name="id_tipo_proceso")
	private TipoProceso tipoProceso;

	private Boolean responsable;

	@Column(name="para_estado")
	private Character paraEstado;

	public RecursoPorPerfil() {

	}

	public RecursoPorPerfil(Recurso recurso,Perfil perfil){
		id=new RecursoPorPerfilPK(recurso,perfil);
	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((id == null) ? 0 : id.hashCode());
		result=prime * result + ((tipoProceso == null) ? 0 : tipoProceso.hashCode());
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
		RecursoPorPerfil other=(RecursoPorPerfil) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		if(tipoProceso == null){
			if(other.tipoProceso != null)
				return false;
		}
		else if(!tipoProceso.equals(other.tipoProceso))
			return false;
		return true;
	}
}