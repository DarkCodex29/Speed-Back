package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
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
import javax.persistence.UniqueConstraint;

@Data
@Entity
@AllArgsConstructor
@Table(name="FERIADO", uniqueConstraints=@UniqueConstraint(columnNames={"fecha","id_sede"}))
public class Feriado implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_feriado",nullable=false)
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@Column(name="fecha_creacion")
	private Date fechaCreacion;
	
	private Character estado;
	
	@ManyToOne
	@JoinColumn(name="id_sede")
	private Sede sede;

	public Feriado() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
