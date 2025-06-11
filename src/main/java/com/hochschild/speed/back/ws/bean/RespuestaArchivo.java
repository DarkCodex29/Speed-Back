package com.hochschild.speed.back.ws.bean;

public class RespuestaArchivo extends Respuesta{
	
	private Integer idArchivo;
	
	public RespuestaArchivo(){}

	public RespuestaArchivo(int error,String mensaje,Integer idArchivo){
		super(error,mensaje);
		this.idArchivo=idArchivo;
	}

	public Integer getIdArchivo(){
		return idArchivo;
	}

	public void setIdArchivo(Integer idArchivo){
		this.idArchivo=idArchivo;
	}
	
}
