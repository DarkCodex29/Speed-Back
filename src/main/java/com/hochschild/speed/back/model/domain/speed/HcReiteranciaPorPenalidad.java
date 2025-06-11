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
@Table(name="HC_REITERANCIA_POR_PENALIDAD")
public class HcReiteranciaPorPenalidad implements Entidad{

	@Id 	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reiterancia_x_penalidad", nullable=false)
	private Integer id;
	 
	@ManyToOne
	@JoinColumn(name="penalidad_por_documento_legal")
	private HcPenalidadPorDocumentoLegal hcPenalidadPorDocumentoLegal;
	
	private String reiterancia;
	
	@Column(name="tipo_valor")
	private String tipoValor;
	
	private String valor;
	
	@Column(name="indice")
	private Integer index;

	public HcReiteranciaPorPenalidad() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
