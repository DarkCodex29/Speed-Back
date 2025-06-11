package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@Table(name="HC_AREA")
public class HcArea implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_hc_area", nullable=false)
	private Integer id;

	@Column(length=200, nullable=false)
	private String nombre;

	@ManyToOne
	@JoinColumn(name="compania", nullable=false)
	private HcCompania compania;

	@Column(nullable=false)
	private Character estado;

	@Column(length=20)
	private String codigo;

	public HcArea() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}