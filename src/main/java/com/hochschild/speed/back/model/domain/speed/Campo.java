package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="CAMPO")
public class Campo implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_campo",nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_tipo_campo",nullable=false)
	private TipoCampo tipoCampo;
	
	@Column(length=100)
	private String nombre;
	
	@Column(length=200)
	private String descripcion;
	
	@Column(length=20)
	private String etiqueta;

	private boolean buscable;

	private boolean obligatorio;
	
	@Column(length=30)
	private String contenido;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	private transient List<Parametro> valores;

	public String getFechaCreacionFormat(){
		return DateUtil.convertDateToString(this.fechaCreacion,DateUtil.FORMAT_DATE);
	}

	public Campo() {

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
		Campo other=(Campo) obj;
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