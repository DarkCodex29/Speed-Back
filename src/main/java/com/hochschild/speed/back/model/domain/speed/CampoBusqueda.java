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
@Table(name = "CAMPO_BUSQUEDA")
public class CampoBusqueda implements Entidad {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_campo_busqueda",nullable=false)
	private Integer id;
	
	@Column(name="valor",length=100)
	private String valor;
	
	@Column(name="campo",length=100)
	private String campo;
	
	@ManyToOne
	@JoinColumn(name="id_busqueda",nullable=false)
	private Busqueda busqueda;

	public CampoBusqueda() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}