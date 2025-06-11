package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class UsuarioEnvioPendientePK implements Serializable{
	private static final long serialVersionUID=-8891352781176045211L;

	@ManyToOne
	@JoinColumn(name="id_envio_pendiente",nullable=false)
	private EnvioPendiente envioPendiente;
	
	@ManyToOne
	@JoinColumn(name="id_destinatario",nullable=false)
	private Usuario destinatario;

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((destinatario == null) ? 0 : destinatario.hashCode());
		result=prime * result + ((envioPendiente == null) ? 0 : envioPendiente.hashCode());
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
		UsuarioEnvioPendientePK other=(UsuarioEnvioPendientePK) obj;
		if(destinatario == null){
			if(other.destinatario != null)
				return false;
		}
		else if(!destinatario.equals(other.destinatario))
			return false;
		if(envioPendiente == null){
			if(other.envioPendiente != null)
				return false;
		}
		else if(!envioPendiente.equals(other.envioPendiente))
			return false;
		return true;
	}
}