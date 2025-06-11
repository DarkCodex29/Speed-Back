package com.hochschild.speed.back.model.domain.speed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@Table(name="DIRECCION")
public class Direccion implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_direccion",nullable=false)
	private Integer id;

	@Column(length=400)
	private String direccion;

	@Column(length=150)
	private String referencia;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name="ubigeo")
	private Ubigeo distrito;

	public Direccion() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}