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


@Data
@Entity
@AllArgsConstructor
@Table(name = "ALERTA_ENVIADA")
public class AlertaEnviada implements Entidad {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_alerta_enviada",nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_alerta",nullable=false)
	private Alerta alerta;
	
	@Column(name = "fecha_enviada")
	private Date fecha;

	@Column(name = "instancia")
	private Integer instancia;

	public AlertaEnviada() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
