package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ResponsableUsuarioPK implements Serializable{

	private static final long serialVersionUID=387745951121735059L;

	@Column(name="id_proceso",nullable=false)
	private Integer idProceso;

	@Column(name="id_usuario",nullable=false)
	private Integer idUsuario;
}