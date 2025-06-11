package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="MENU")
public class Menu extends Recurso implements Serializable{

	private static final long serialVersionUID=-6361968999645746806L;

	@Column(length=256)
	private String url;
	
	@ManyToOne
	@JoinColumn(name="men_id_recurso")
	private Menu padre;

	@OneToMany(mappedBy="padre")
	private List<Menu> hijos;

	private Boolean defecto;

	@Column(length=50)
	private String tipo;

	private Boolean cuenta;

	private Integer orden;

	public Menu() {

	}
}
