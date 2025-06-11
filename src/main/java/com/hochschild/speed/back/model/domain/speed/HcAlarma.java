package com.hochschild.speed.back.model.domain.speed;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;

import lombok.Data;


@Data
@Entity
@AllArgsConstructor
@Table(name="HC_ALARMA")
public class HcAlarma implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_alarma", nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_documento_legal", nullable=false)
	@JsonBackReference
	private HcDocumentoLegal documentoLegal;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_alarma", nullable=false)
	private Date fechaAlarma;
	
	@Column(nullable=false)
	private Integer dias_activacion;
	
	@Column(nullable=false)
	private Integer dias_intervalo;
	
	@Column(length=200, nullable=false)
	private String titulo;
	
	@Column(columnDefinition="TEXT", length=4000, nullable=false)
	private String mensaje;
	
	@Column(nullable=false)
	private Character estado;

	@Column
	private Boolean anual;

	public String getEstadoDescripcion() {
		return (this.estado!=null&&this.estado.equals('A'))? "Activo":"Inactivo";
	}

	public HcAlarma() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}