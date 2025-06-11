package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@Table(name="PASO_XOR")
public class PasoXOR extends PasoWorkflow{
	
	@Column(nullable=false)
	private Character tipo;
	
	@Column(length=200,nullable=false)
	private String dato;
	
	@Column(length=5)
	private String operador;
	
	@Column(length=200)
	private String valor;
	
	@ManyToOne
	@JoinColumn(name="paso_alternativo")
	private PasoWorkflow alternativo;
	
	@Column(name="tipo_siguiente")
	private Character tipoSiguiente;
	
	@Column(name="tipo_alternativo")
	private Character tipoAlternativo;
	
	private transient int pasoSiguiente;
	
	private transient int pasoAlternativo;

	public PasoXOR() {

	}
}