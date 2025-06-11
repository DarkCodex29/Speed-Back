package com.hochschild.speed.back.model.domain.speed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@Entity
@Table(name="PROCESO")
public class Proceso implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_proceso",nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_tipo_proceso",nullable=false)
	private TipoProceso tipoProceso;

	@Column(name="id_proceso_padre")
	private Integer idProcesoPadre;

	@Column(length=200, nullable=false)
	private String nombre;

	@Column(length=200)
	private String descripcion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;

	private Character estado;

	@Column(name="nombre_intalio",length=100)
	private String nombreIntalio;

	private Integer plazo;
	
	private Boolean cliente;
	
	@Column(name="tipo_confidencialidad")
	private Character tipoConfidencialidad;
	
	@Column(name="creador_responsable")
	private boolean creadorResponsable;

	@ManyToMany
	@JoinTable(name="RESPONSABLE_USUARIO",joinColumns={@JoinColumn(name="id_proceso")},inverseJoinColumns={@JoinColumn(name="id_usuario")})
	private List<Usuario> usuariosResponsables;

	@ManyToMany
	@JoinTable(name="RESPONSABLE_ROL",joinColumns={@JoinColumn(name="id_proceso")},inverseJoinColumns={@JoinColumn(name="id_rol")})
	private List<Rol> rolesResponsables;

	@ManyToMany
	@JoinTable(name="USUARIO_POR_PROCESO",joinColumns={@JoinColumn(name="proceso")},inverseJoinColumns={@JoinColumn(name="usuario")})
	private List<Usuario> usuariosParticipantes;

	@ManyToMany
	@JoinTable(name="ROL_POR_PROCESO",joinColumns={@JoinColumn(name="proceso")},inverseJoinColumns={@JoinColumn(name="rol")})
	private List<Rol> rolesParticipantes;

	@ManyToMany
	@JoinTable(name="ROL_CREADOR_PROCESO",joinColumns={@JoinColumn(name="proceso")},inverseJoinColumns={@JoinColumn(name="rol")})
	private List<Rol> rolesCreadores;


	@OneToMany(mappedBy="proceso")
	private List<FechaRecomendadaProceso> fechas;

	@JsonIgnore
	@ManyToMany(mappedBy="procesos")
	private List<Reemplazo> reemplazos;

	public Proceso() {

	}

	public Proceso(Integer id){
		this.id=id;
	}

	public Proceso(Integer id,String nombre){
		this.id=id;
		this.nombre=nombre;
	}

	public Proceso(String nombre){
		this.nombre=nombre;
	}

	public Proceso(Integer id,String nombre,List<Usuario> usuariosResponsables){
		this.id=id;
		this.nombre=nombre;
		this.usuariosResponsables=usuariosResponsables;
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
		Proceso other=(Proceso) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		return true;
	}

	public String getLabel(){
		String label=nombre;
		if(tipoProceso!=null){
			label+=" - "+tipoProceso.getLabel();
		}
		return label;
	}

	@Override
	public String toString(){
		return "Proceso [id=" + id + ", nombre=" + nombre + ", plazo=" + plazo + ", tipoConfidencialidad=" + tipoConfidencialidad + "]";
	}
}