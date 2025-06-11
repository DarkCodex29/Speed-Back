package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="AREA")
public class Area implements Entidad,Serializable{

	private static final long serialVersionUID=7511998874107554048L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_area",nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_sede",nullable=false)
	private Sede sede;

	private Integer dependencia;

	@Column(length=100)
	private String nombre;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="hora_ingreso")
	private Date horaIngreso;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="hora_salida")
	private Date horaSalida;
	
	private transient List<Area> hijos;

	public Area() {

	}

	public Area(Integer id, String nombre){
		this.id=id;
		this.nombre=nombre;
	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Area other=(Area) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String getLabel() {
		return null;
	}
}