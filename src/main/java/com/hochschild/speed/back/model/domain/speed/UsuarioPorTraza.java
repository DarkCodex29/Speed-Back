package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@Entity
@Table(name="USUARIO_POR_TRAZA")
public class UsuarioPorTraza{

	@EmbeddedId
	private UsuarioPorTrazaPK id;

	private boolean responsable;

	private Boolean aprobado;

	private boolean leido;

	private boolean bloqueado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_aprobacion")
	private Date fechaAprobacion;

	@ManyToOne
	@JoinColumn(name="id_rol")
	private Rol rol;

	private Character estado;

	public UsuarioPorTraza() {

	}
	public UsuarioPorTraza(Usuario u,Traza traza){
		id=new UsuarioPorTrazaPK(traza,u);
	}

	public UsuarioPorTraza(Integer idUsuario,Integer idTraza){
		id=new UsuarioPorTrazaPK(idUsuario,idTraza);
	}
}