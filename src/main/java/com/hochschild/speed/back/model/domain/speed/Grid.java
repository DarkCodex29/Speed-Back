package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@Table(name="GRID")
public class Grid extends Recurso implements Serializable{

	private static final long serialVersionUID=-1265089061349939203L;

	@Column(length=30)
	private String codigo;

	@Column(name="para_estado")
	private Character paraEstado;
	
	@Column(length=200)
	private String titulo;
	
	@Column(nullable=true)
	private boolean multiple;

	@OneToMany(mappedBy="grid")
	private List<Columna> columnas;
	
	private transient List<Boton> botones;

	public Grid() {

	}
}
