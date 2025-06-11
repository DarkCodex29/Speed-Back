package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@Table(name="TIPO_NOTIFICACION")
public class TipoNotificacion implements Entidad{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_notificacion",nullable=false)
	private Integer id;
	
	@Column(length=100)
	private String nombre;

	@Column(length=200)
	private String descripcion;

	private Character estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@Column(length=255)
	private String codigo;

	public TipoNotificacion() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}