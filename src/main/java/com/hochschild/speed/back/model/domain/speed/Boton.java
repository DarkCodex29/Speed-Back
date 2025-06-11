package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@Table(name="BOTON")
public class Boton extends Recurso{
	
	@Column(length=256)
	private String url;

	@Column(name="codigo_grid",length=20)
	private String codigo;
	
	@Column(length=255)
	private String tipo;
	
	@Column(length=30)
	private String icono;

	@Column(length=20)
	private String parametro;

	private Boolean bloqueable;

	private Boolean verParalela;

	@Column(name="on_submit",length=30)
	private String onSubmit;

	@Column(name="on_complete",length=20)
	private String onComplete;

	private Integer orden;

	@Column(length=60)
	private String iconoNuevo;

	@Column(length=60)
	private String botonClaseNuevo;

	@Column(length=60)
	private String urlNuevo;

	private transient boolean bloqueado;

	public Boton() {

	}
}