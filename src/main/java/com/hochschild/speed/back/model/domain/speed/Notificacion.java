package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Data
@Entity
@AllArgsConstructor
@Table(name = "NOTIFICACION")
public class Notificacion implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_notificacion",nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_notificacion",nullable=false)
	private TipoNotificacion tipoNotificacion;

	@ManyToOne
	@JoinColumn(name="id_traza")
	private Traza traza;

	@ManyToOne
	@JoinColumn(name="id_traza_copia")
	private TrazaCopia trazaCopia;
	
	@ManyToOne
	@JoinColumn(name="id_usuario",nullable=false)
	private Usuario remitente;

	@Column(length=255)
	private String titulo;

	@Column(length=255)
	private String contenido;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;

	private Boolean leido;

	@ManyToMany
	@JoinTable(name="DESTINATARIO_NOTIFICACION",joinColumns={@JoinColumn(name="id_notificacion")},inverseJoinColumns={@JoinColumn(name="id_usuario")})
	private List<Usuario> destinatarios;

	public Notificacion() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}