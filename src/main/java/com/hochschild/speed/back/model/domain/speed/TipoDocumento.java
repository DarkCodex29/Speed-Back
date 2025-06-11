package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@Table(name="TIPO_DOCUMENTO")
public class TipoDocumento implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_documento",nullable=false)
	private Integer id;

	@Column(length=100,nullable=false)
	private String nombre;

	@Column(length=200)
	private String descripcion;

	private Character estado;
	
	@Column(nullable=false)
	private boolean firmable;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;
	
	@ManyToMany
	@JoinTable(name="CAMPO_POR_TIPO_DOCUMENTO",joinColumns={@JoinColumn(name="tipo_documento")},inverseJoinColumns={@JoinColumn(name="campo")})
	private List<Campo> campos;

	public TipoDocumento() {

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
		TipoDocumento other=(TipoDocumento) obj;
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