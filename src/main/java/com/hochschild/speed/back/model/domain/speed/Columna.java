package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Data
@Entity
@AllArgsConstructor
@Table(name="COLUMNA")
public class Columna implements Entidad,Serializable{

	private static final long serialVersionUID=4185458667354370053L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_columna",nullable=false)
	private Integer id;
	
	@Column(length=100, nullable=false)
	private String nombre;

	@Column(length=40)
	private String codigo;
	
	@Column(nullable=false)
	private Integer ancho;
	
	@Column(nullable=false)
	private boolean visible;

	@Column(length=50)
	private String tipo;

	@Column(nullable=false)
	private Integer orden;

	private boolean procesar;

	private boolean filtrable;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	private Character estado;

	private boolean titulo;

	@Column(length=20)
	private String alineacion;
	
	@Column(nullable=false)
	private boolean identificador;
	
	@Column(name="es_estado",nullable=false)
	private boolean esEstado;
	
	@Column(name="left_join",length=100)
	private String leftJoin;

	@ManyToOne
	@JoinColumn(name="id_recurso",nullable=false)
	private Grid grid;
	
	private transient Integer idGrid;

	public Columna() {

	}

	@Override
	public String toString(){
		return "id:" + id + " nombre:" + nombre + " codigo:" + codigo + " titulo:" + titulo;
	}

	public String getLabel(){
		return nombre;
	}
}