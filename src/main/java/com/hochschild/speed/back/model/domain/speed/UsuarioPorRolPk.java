package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class UsuarioPorRolPk implements Serializable{

	private static final long serialVersionUID=-1133740396497094500L;
	
	@ManyToOne
	@JoinColumn(name="rol", nullable=false)
	private Rol rol;
	
	@ManyToOne
	@JoinColumn(name="usuario", nullable=false)
	private Usuario usuario;
}
