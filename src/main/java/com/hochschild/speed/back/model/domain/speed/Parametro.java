package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@Table(name="PARAMETRO")
public class Parametro implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_parametro",nullable=false)
	private Integer id;

	@Column(length=100)
	private String descripcion;

	@Column(length=50)
	private String tipo;

	@Column(length=1024)
	private String valor;

	public Parametro() {
		
	}

	@Override
	public String getLabel() {
		return null;
	}
}