package com.hochschild.speed.back.ws.bean;

import com.hochschild.speed.back.model.domain.speed.Parametro;
import java.util.Date;
import java.util.List;

public class TrazabilidadWs {
	
	private Integer id;
	
	private Date fecha;
	
	private Integer plazo;

	private String accion;

	private String observacion;

	private List<AccionWs> acciones;
	
	private String actividad;
	
	private Parametro prioridad;
	
	private PasoWs paso;
	
	private ProcesoWs proceso;
	
	private UsuarioWS remitente;
	
	private String mensaje;
	
	private List<UsuarioWS> destinatarios;
	
	private UsuarioWS reemplazo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getPlazo() {
		return plazo;
	}

	public void setPlazo(Integer plazo) {
		this.plazo = plazo;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<AccionWs> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<AccionWs> acciones) {
		this.acciones = acciones;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public Parametro getPrioridad(){
		return prioridad;
	}

	public void setPrioridad(Parametro prioridad){
		this.prioridad=prioridad;
	}

	public PasoWs getPaso() {
		return paso;
	}

	public void setPaso(PasoWs paso) {
		this.paso = paso;
	}

	public ProcesoWs getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoWs proceso) {
		this.proceso = proceso;
	}

	public UsuarioWS getRemitente() {
		return remitente;
	}

	public void setRemitente(UsuarioWS remitente) {
		this.remitente = remitente;
	}

	public List<UsuarioWS> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(List<UsuarioWS> destinatarios) {
		this.destinatarios = destinatarios;
	}

	public UsuarioWS getReemplazo() {
		return reemplazo;
	}

	public void setReemplazo(UsuarioWS reemplazo) {
		this.reemplazo = reemplazo;
	}
}
