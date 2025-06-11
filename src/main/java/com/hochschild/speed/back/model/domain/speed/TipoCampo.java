package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name="TIPO_CAMPO")
public class TipoCampo implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_campo",nullable=false)
	private Integer id;

	@Column(nullable=false,length=100)
	private String nombre;

	@Column(nullable=false,length=10)
	private String codigo;
	
	@Column(name="con_contenido",nullable=false)
	private boolean conContenido;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;
	
	@Column(nullable=false)
	private Character estado;

	public TipoCampo() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}