package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@AllArgsConstructor
@Table(name="UBIGEO")
public class Ubigeo implements Entidad,Serializable{

	private static final long serialVersionUID=1205877978523738893L;

	@Id
	@Column(name="id_ubigeo",nullable=false)
	private Integer id;
	
	@Column(length=2,nullable=false)
	private String codigo;
	
	@Column(length=40,nullable=false)
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name="padre")
	private Ubigeo padre;

	public Ubigeo() {

	}

	public Ubigeo(Integer id,String nombre){
		super();
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
		Ubigeo other=(Ubigeo) obj;
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