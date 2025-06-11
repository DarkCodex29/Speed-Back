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
@Table(name = "HC_UBICACION")
public class HcUbicacion implements Entidad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ubicacion", nullable = false)
	private Integer id;

	@Column(nullable = false, length = 200)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "tipo_ubicacion", nullable = false)
	private HcTipoUbicacion tipo_ubicacion;

	@ManyToOne
	@JoinColumn(name = "compania", nullable = false)
	private HcCompania compania;

	@Column(nullable = false)
	private Character estado;

	@Column(length=20)
	private String codigo;

	public HcUbicacion() {

	}

	public HcUbicacion(Integer id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	public HcUbicacion(String nombre) {
		super();
		this.nombre = nombre;
	}

	@Override
	public String getLabel() {
		return null;
	}
}