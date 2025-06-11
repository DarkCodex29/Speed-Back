package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@Table(name="PERFIL")
public class Perfil implements Entidad,Serializable{

	private static final long serialVersionUID=-5830564304328433248L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_perfil",nullable=false)
	private Integer id;

	@Column(length=100,nullable=false)
	private String nombre;
	
	@Column(length=200)
	private String descripcion;

	@Column(nullable=false)
	private Character estado;
	
	@Column(length=15)
	private String codigo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	public Perfil() {

	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Perfil other=(Perfil) obj;
		if(codigo == null){
			if(other.codigo != null)
				return false;
		}
		else if(!codigo.equals(other.codigo))
			return false;
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