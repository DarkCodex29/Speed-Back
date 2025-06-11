package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name = "FECHA_RECOMENDADA_PROCESO")
public class FechaRecomendadaProceso implements Entidad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fecha_recomendada", nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_proceso")
	private Proceso proceso;

	private Integer dia;

	private Integer mes;

	public FechaRecomendadaProceso() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}