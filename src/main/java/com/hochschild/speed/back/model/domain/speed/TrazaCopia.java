package com.hochschild.speed.back.model.domain.speed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Data;


@Data
@Entity
@AllArgsConstructor
@Table(name = "TRAZA_COPIA")
public class TrazaCopia implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_traza_copia", nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="destinatario")
	private Usuario destinatario;
	
	@ManyToOne
	@JoinColumn(name="id_traza_referencia")
	private Traza referencia;
	
	@Column(name="tipo_referencia", columnDefinition="char(1)")
	private String tipoReferencia;
	
	private Integer prioridad;

	public TrazaCopia() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
