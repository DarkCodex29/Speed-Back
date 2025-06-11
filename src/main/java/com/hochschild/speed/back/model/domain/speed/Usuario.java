package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="USUARIO")
public class Usuario implements Entidad,Serializable{

	private static final long serialVersionUID=-8775360989495203746L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario",nullable=false)
	private Integer id;

	@Column(name="usuario",length=20,nullable=false,unique=true)
	private String usuario;
	
	@Column(length=256 ,nullable=false)
	private String clave;

	@Column(length=100)
	private String nombres;
	
	@Column(length=100)
	private String apellidos;

	@Column(length=100)
	private String correo;

	@ManyToOne
	@JoinColumn(name = "area")
	private Area area;

	private Character estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;
	
	@ManyToOne
	@JoinColumn(name="id_jefe")
	private Usuario jefe;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USUARIO_POR_ROL",joinColumns={@JoinColumn(name="usuario")},inverseJoinColumns={@JoinColumn(name="rol")})
	private List<Rol> roles;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USUARIO_POR_PERFIL",joinColumns={@JoinColumn(name="id_usuario")},inverseJoinColumns={@JoinColumn(name="id_perfil")})
	private List<Perfil> perfiles;

	@Column(name="requiere_aprobacion")
	private boolean requiereAprobacion;

	public Usuario() {

	}

	public Usuario(Integer id,String nombres,String apellidos){
		this.id=id;
		this.nombres=nombres;
		this.apellidos=apellidos;
	}

	public Usuario(Integer id,String nombres,String apellidos, Integer idArea, String area){
		this.id=id;
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.area=new Area(idArea, area);
	}

	public Usuario(Integer id,String nombres,String apellidos,String correo){
		super();
		this.id=id;
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.correo=correo;
	}

	public Usuario(Integer id,String usuario,String nombres,String apellidos,String correo){
		super();
		this.id=id;
		this.usuario=usuario;
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.correo=correo;
	}

	public Usuario(Integer id,String usuario,String nombres,String apellidos,String correo, Integer idArea, String area){
		super();
		this.id=id;
		this.usuario=usuario;
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.correo=correo;
		this.area=new Area(idArea, area);
	}

	public Usuario(Integer idUsuario){
		this.id=idUsuario;
	}

	public String getNombreCompleto(){
		return nombres + " " + apellidos;
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
		Usuario other=(Usuario) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		return true;
	}

	public String getLabel(){
		String label = nombres + " " + apellidos;
		label += area != null? " ("+area.getNombre()+")": "";
		return label;
	}
}