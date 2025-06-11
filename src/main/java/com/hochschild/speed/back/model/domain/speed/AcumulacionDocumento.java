package com.hochschild.speed.back.model.domain.speed;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Data;


@Data
@Entity
@AllArgsConstructor
@Table(name="ACUMULACION_DOCUMENTO")
public class AcumulacionDocumento implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_acumulacion_documento",nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_documento")
	private Documento documento;
	
	@ManyToOne
	@JoinColumn(name="id_expediente_origen")
	private Expediente expedienteOrigen;
	
	@ManyToOne
	@JoinColumn(name="id_expediente_destino")
	private Expediente expedienteDestino;
	
	@Column(name="fecha_creacion")
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name="id_usuario_accion")
	private Usuario usuario;

	public AcumulacionDocumento() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}