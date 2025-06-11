package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name = "ENVIO_PENDIENTE")
public class EnvioPendiente implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_envio_pendiente",nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_expediente",nullable=false)
	private Expediente expediente;
	
	@Column(length=4000)
	private String observacion;
	
	@ManyToOne
	@JoinColumn(name="prioridad")
	private Parametro prioridad;
	
	private Integer plazo;
	
	@Column(name="para_aprobar")
	private boolean paraAprobar;
	
	@ManyToOne
	@JoinColumn(name="remitente",nullable=false)
	private Usuario remitente;
	
	@ManyToOne
	@JoinColumn(name="id_jefe",nullable=false)
	private Usuario jefe;
	
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;
	
	@Column(nullable=false)
	private Character estado;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="ACCION_ENVIO_PENDIENTE",joinColumns={@JoinColumn(name="id_envio_pendiente")},inverseJoinColumns={@JoinColumn(name="id_accion")})
	private List<Accion> acciones;
	
	@ManyToOne
	@JoinColumn(name="rol")
	private Rol rol;
	
	private transient List<UsuarioEnvioPendiente> usuarios;

	public EnvioPendiente() {

	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((id == null) ? 0 : id.hashCode());
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
		EnvioPendiente other=(EnvioPendiente) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String getLabel() {
		return null;
	}
}