package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_NUMERACION")
public class HcNumeracion{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_numeracion", nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_compania", nullable=false)
	private HcCompania compania;

	@ManyToOne
	@JoinColumn(name="id_contrato")
	private HcContrato contrato;
		
	@Column(nullable=false)
	private Integer secuencia;
	
	@Column(nullable=false)
	private Integer anio;

	public HcNumeracion() {

	}
}