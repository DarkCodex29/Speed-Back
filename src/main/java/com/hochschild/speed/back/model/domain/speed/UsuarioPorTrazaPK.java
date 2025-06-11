package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Data
@Embeddable
public class UsuarioPorTrazaPK implements Serializable{
	
	private static final long serialVersionUID=-1133740396497094500L;
	
	@ManyToOne
	@JoinColumn(name="traza",nullable=false)
	private Traza traza;
	
	@ManyToOne
	@JoinColumn(name="usuario",nullable=false)
	private Usuario usuario;
	public UsuarioPorTrazaPK() {

	}

	public UsuarioPorTrazaPK(Traza traza, Usuario usuario) {
		super();
		this.traza = traza;
		this.usuario = usuario;
	}

	public UsuarioPorTrazaPK(Integer idUsuario,Integer idTraza){
		this.usuario=new Usuario(idUsuario);
		this.traza=new Traza(idTraza);
	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((traza == null) ? 0 : traza.hashCode());
		result=prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		UsuarioPorTrazaPK other=(UsuarioPorTrazaPK) obj;
		if(traza == null){
			if(other.traza != null)
				return false;
		}
		else if(!traza.equals(other.traza))
			return false;
		if(usuario == null){
			if(other.usuario != null)
				return false;
		}
		else if(!usuario.equals(other.usuario))
			return false;
		return true;
	}
}