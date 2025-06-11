package com.hochschild.speed.back.model.domain.speed.utils;

public class InteresadoDTS {

	private String tipo;
	
	private Integer id;
	
	private String nombre;

	public InteresadoDTS(){
		
	}
	
	public InteresadoDTS(String tipo, Integer id, String nombre){
		this.tipo=tipo;
		this.id=id;
		this.nombre=nombre;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getLabel() {
		return nombre;
	}
}
