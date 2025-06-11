package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@Table(name="TIPO_ALERTA")
public class TipoAlerta implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_alerta",nullable=false)
	private Integer id;

	@Column(length=100)
	private String nombre;

	@Column(length=50)
	private String imagen;

	@Column(name="porcentaje_intervalo")
	private Integer porcentaje;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	private boolean defecto;

	public TipoAlerta() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}