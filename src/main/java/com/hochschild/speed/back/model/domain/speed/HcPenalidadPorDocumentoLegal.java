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
@Table(name="HC_PENALIDAD_POR_DOCUMENTO_LEGAL")
public class HcPenalidadPorDocumentoLegal implements Entidad{

	@Id 	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_penalidad_p_dl", nullable=false)
	private Integer id;
	 
	@ManyToOne
	@JoinColumn(name="id_documento_legal")
	private HcDocumentoLegal documentoLegal;
	
	@ManyToOne
	@JoinColumn(name="id_penalidad")
	private HcPenalidad penalidad;
	
	private Integer reiterancia;
	
	private boolean aplica;

	public HcPenalidadPorDocumentoLegal() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
