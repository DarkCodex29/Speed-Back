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
@Table(name = "MENSAJERIA_CARGO")
public class CargoMensajeria implements Entidad{

	@Id
	@Column(name="id_mensajeria",nullable=false)
	private Integer id;

	@Column(name="costo_envio",nullable=false, columnDefinition="FLOAT")
	private Double costoEnvio;
	
	@ManyToOne
	@JoinColumn(name="id_estado_cargo",nullable=false)
	private Parametro estadoCargo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_cargo",nullable=false)
	private Date fechaCargo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_devolucion_area",nullable=false)
	private Date fechaDevolucionArea;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_entrega")
	private Date fechaEntrega;
	
	@Column(name="recibido_por",length=100)
	private String recibidoPor;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_devolucion_cargo")
	private Date fechaDevolucionCargo;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_devolucion_documento")
	private Date fechaDevolucionDocumento;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_primera_visita")
	private Date fechaPrimeraVisita;
	
	@Column(name="observacion_primera_visita",length=200)
	private String observacionPrimeraVisita;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_segunda_visita")
	private Date fechaSegundaVisita;
	
	@Column(name="observacion_segunda_visita",length=200)
	private String observacionSegundaVisita;
	
	@Column(length=200,nullable=false)
	private String observaciones;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;

	@OneToOne
	@JoinColumn(name="id_mensajeria")
	private Mensajeria mensajeria;

	public CargoMensajeria() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}