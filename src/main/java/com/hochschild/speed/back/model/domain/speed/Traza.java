package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name="TRAZA")
public class Traza implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_traza",nullable=false)
	private Integer id;

	private boolean actual;

	@Column(length=20, nullable=false)
	private String accion;

	@Column(columnDefinition="TEXT",length=4000)
	private String observacion;

	@Column(length=200)
	private String actividad;

	@ManyToOne
	@JoinColumn(name="prioridad")
	private Parametro prioridad;

	private Integer orden;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;

	@ManyToOne
	@JoinColumn(name="id_expediente",nullable=false)
	private Expediente expediente;

	@ManyToOne
	@JoinColumn(name="id_paso")
	private PasoWorkflow paso;

	@ManyToOne
	@JoinColumn(name="remitente")
	private Usuario remitente;

	@ManyToOne
	@JoinColumn(name="id_proceso")
	private Proceso proceso;

	@ManyToOne
	@JoinColumn(name="reemplazado")
	private Usuario reemplazado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_limite")
	private Date fechaLimite;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="ACCION_TRAZA",joinColumns={@JoinColumn(name="id_traza")},inverseJoinColumns={@JoinColumn(name="id_accion")})
	private List<Accion> accionTraza;

	private Character estado;

	@ManyToOne
	@JoinColumn(name="padre")
	private Traza padre;

	private transient List<Usuario> destinatarios;
	
	private transient Integer pasoVisado;

	public Traza() {

	}

	public Traza(Integer idTraza){
		this.id=idTraza;
	}

    @Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((id == null) ? 0 : id.hashCode());
		result=prime * result + ((remitente == null) ? 0 : remitente.hashCode());
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
		Traza other=(Traza) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		if(remitente == null){
			if(other.remitente != null)
				return false;
		}
		else if(!remitente.equals(other.remitente))
			return false;
		return true;
	}

	@Override
	public String getLabel() {
		return null;
	}
}