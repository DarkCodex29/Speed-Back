package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class RecursoPorPerfilPK implements Serializable{

	private static final long serialVersionUID=-1133740396497094500L;

	@ManyToOne
	@JoinColumn(name="id_recurso",nullable=false)
	private Recurso recurso;
	
	@ManyToOne
	@JoinColumn(name="id_perfil",nullable=false)
	private Perfil perfil;

	public RecursoPorPerfilPK() {
	}

	public RecursoPorPerfilPK(Recurso recurso, Perfil perfil) {
		this.recurso = recurso;
		this.perfil = perfil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
		result = prime * result + ((recurso == null) ? 0 : recurso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecursoPorPerfilPK other = (RecursoPorPerfilPK) obj;
		if (perfil == null) {
			if (other.perfil != null)
				return false;
		} else if (!perfil.equals(other.perfil))
			return false;
		if (recurso == null) {
			if (other.recurso != null)
				return false;
		} else if (!recurso.equals(other.recurso))
			return false;
		return true;
	}
}