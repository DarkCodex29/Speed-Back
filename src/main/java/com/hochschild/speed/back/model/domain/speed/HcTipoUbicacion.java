package com.hochschild.speed.back.model.domain.speed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Data;


@Data
@Entity
@AllArgsConstructor
@Table(name="HC_TIPO_UBICACION")
public class HcTipoUbicacion implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_ubicacion", nullable=false)
	private Integer id;
	
	@Column(nullable=false, length=200)
	private String nombre;

	@Column(nullable=false, length=20)
	private String codigo;

	public HcTipoUbicacion() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}