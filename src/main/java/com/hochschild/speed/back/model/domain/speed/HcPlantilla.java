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
@Table(name = "HC_PLANTILLA")
public class HcPlantilla implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_plantilla", nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="tipo_contrato",nullable=false)
	private HcTipoContrato tipoContrato;
	
	@Column(nullable=false, length=200)
	private String nombre;
	
	@Column(nullable=false, length=2000)
	private String ruta;
	
	@Column(nullable=false)
	private Character estado;

	public HcPlantilla() {

	}

	public HcPlantilla(Integer id, String nombre, String ruta) {
		this.id = id;
		this.nombre = nombre;
		this.ruta = ruta;
	}

	@Override
	public String getLabel() {
		return null;
	}
}