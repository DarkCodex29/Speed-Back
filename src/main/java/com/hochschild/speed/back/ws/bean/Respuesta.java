package com.hochschild.speed.back.ws.bean;

public class Respuesta{
	
	private int error;
	
	private String mensaje;
	
	public Respuesta(){}

	public Respuesta(int error,String mensaje){
		super();
		this.error=error;
		this.mensaje=mensaje;
	}

	public int getError(){
		return error;
	}

	public void setError(int error){
		this.error=error;
	}

	public String getMensaje(){
		return mensaje;
	}

	public void setMensaje(String mensaje){
		this.mensaje=mensaje;
	}

}
