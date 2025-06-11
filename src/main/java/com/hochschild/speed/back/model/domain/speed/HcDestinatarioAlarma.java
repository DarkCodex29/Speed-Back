package com.hochschild.speed.back.model.domain.speed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="HC_DESTINATARIO_ALARMA")
public class HcDestinatarioAlarma implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_destinatario_alarma", nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_alarma",nullable=false)
	private HcAlarma alarma;
	
	@ManyToOne
	@JoinColumn(name="id_grupo")
	private HcGrupo grupo;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	public HcDestinatarioAlarma() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
