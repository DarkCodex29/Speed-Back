package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class BotonPorGridPorPerfilPK implements Serializable{

	private static final long serialVersionUID=6861115376318771975L;
	

	@ManyToOne
	@JoinColumn(name="id_boton",nullable=false)
	private Boton boton;

	@ManyToOne
	@JoinColumn(name="id_grid",nullable=false)
	private Grid grid;

	@ManyToOne
	@JoinColumn(name="id_perfil",nullable=false)
	private Perfil perfil;

	public BotonPorGridPorPerfilPK() {
	}

	public BotonPorGridPorPerfilPK(Boton boton, Grid grid, Perfil perfil) {
		super();
		this.boton = boton;
		this.grid = grid;
		this.perfil = perfil;
	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((boton == null) ? 0 : boton.hashCode());
		result=prime * result + ((grid == null) ? 0 : grid.hashCode());
		result=prime * result + ((perfil == null) ? 0 : perfil.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		BotonPorGridPorPerfilPK other=(BotonPorGridPorPerfilPK) obj;
		if(boton == null){
			if(other.boton != null)
				return false;
		}
		else if(!boton.equals(other.boton))
			return false;
		if(grid == null){
			if(other.grid != null)
				return false;
		}
		else if(!grid.equals(other.grid))
			return false;
		if(perfil == null){
			if(other.perfil != null)
				return false;
		}
		else if(!perfil.equals(other.perfil))
			return false;
		return true;
	}
}