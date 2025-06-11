package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@Table(name="RECURSO")
@Inheritance(strategy=InheritanceType.JOINED)
public class Recurso implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_recurso",nullable=false)
	private Integer id;
	
	@Column(length=100,nullable=false)
	private String nombre;
	
	@Column(length=200)
	private String descripcion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	private Character estado;

	private Boolean movil;

	public Recurso() {

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
		Recurso other=(Recurso) obj;
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