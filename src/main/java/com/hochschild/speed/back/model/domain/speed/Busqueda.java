package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.io.Serializable;
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
@Table(name = "BUSQUEDA")
public class Busqueda implements Entidad,Serializable{

	private static final long serialVersionUID=-8627862956677606095L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_busqueda",nullable=false)
	private Integer id;

	@Column(name="nombre",length=100)
	private String nombre;

	private Character estado;

	@ManyToOne
	@JoinColumn(name="id_usuario",nullable=false)
	private Usuario usuario;

	public Busqueda() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}