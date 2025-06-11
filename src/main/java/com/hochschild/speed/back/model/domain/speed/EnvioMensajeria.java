package com.hochschild.speed.back.model.domain.speed;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;

import lombok.Data;


@Data
@Entity
@AllArgsConstructor
@Table(name="MENSAJERIA_ENVIO")
public class EnvioMensajeria implements Entidad{

	@Id
	@Column(name="id_mensajeria",nullable=false)
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_recepcion",nullable=false)
	private Date fechaRecepcion;	

	@Column(name="numero_guia",length=15,nullable=false)
	private String numeroGuia;

	@Column(nullable=false, columnDefinition="FLOAT")
	private Double peso;
	
	@ManyToOne
	@JoinColumn(name="id_unidad_peso",nullable=false)
	private Parametro unidadPeso;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_courier",nullable=false)
	private Parametro tipoCourier;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;

	@OneToOne
	@JoinColumn(name="id_mensajeria")
	private Mensajeria mensajeria;

	public EnvioMensajeria() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}