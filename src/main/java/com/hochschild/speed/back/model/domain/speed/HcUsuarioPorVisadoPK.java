package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class HcUsuarioPorVisadoPK implements Serializable{
	
	private static final long serialVersionUID=-1133740396497094500L;
	
	@ManyToOne
	@JoinColumn(name="id_visado",nullable=false)
	private HcVisado visado;
	
	@ManyToOne
	@JoinColumn(name="id_visador",nullable=false)
	private Usuario usuario;

	public HcUsuarioPorVisadoPK(HcVisado visado,Usuario usuario){
		this.visado=visado;
		this.usuario=usuario;
	}
	public HcUsuarioPorVisadoPK(){

	}
}