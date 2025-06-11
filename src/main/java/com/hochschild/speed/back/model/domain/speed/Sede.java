package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name="SEDE")
public class Sede implements Entidad,Serializable{

	private static final long serialVersionUID=-8497384369485714856L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_sede",nullable=false)
	private Integer id;

	@Column(length=100)
	private String nombre;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@OneToOne
	@JoinColumn(name="ubigeo")
	private Ubigeo ubigeo;

	private Character estado;

	public Sede() {

	}

	@Override
	public String getLabel() {
		return null;
	}

	public String getFechaCreacionLabel(){
		return DateUtil.convertDateToString(this.fechaCreacion,DateUtil.FORMAT_DATE);
	}
}