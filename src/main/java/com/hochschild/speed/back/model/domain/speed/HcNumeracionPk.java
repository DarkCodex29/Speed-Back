package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class HcNumeracionPk implements Serializable{
	
	private static final long serialVersionUID=-1133740396497094500L;
	
	@ManyToOne
	@JoinColumn(name="id_compania", nullable=false)
	private HcCompania id_compania;
	
	@ManyToOne
	@JoinColumn(name="id_contrato", nullable=false)
	private HcContrato id_contrato;
}