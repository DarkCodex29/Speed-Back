package com.hochschild.speed.back.ws.bean;

public class UsuarioWS {
	
	private Integer id;
	
	private String usuario;
	
	private String nombreCompleto;
	
	private String area;
	
	private String rolDerivacion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuario(){
		return usuario;
	}

	public void setUsuario(String usuario){
		this.usuario=usuario;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRolDerivacion() {
		return rolDerivacion;
	}

	public void setRolDerivacion(String rolDerivacion) {
		this.rolDerivacion = rolDerivacion;
	}
	
	

}
