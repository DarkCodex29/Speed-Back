package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@Table(name="ACCION")
public class Accion implements Entidad{

	@Id 	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_accion", nullable=false)
	private Integer id;
	
	@Column(length=50)
	private String nombre;

	public Accion() {

	}

	@Override
	public String getLabel() {
		return this.nombre;
	}
}