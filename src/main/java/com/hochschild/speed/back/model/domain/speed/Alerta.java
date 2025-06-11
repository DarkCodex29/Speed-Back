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
@Table(name="ALERTA")
public class Alerta implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_alerta",nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_tipo_alerta",nullable=false)
	private TipoAlerta tipoAlerta;

	@ManyToOne
	@JoinColumn(name="id_grid")
	private Grid grid;

	public Alerta() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}