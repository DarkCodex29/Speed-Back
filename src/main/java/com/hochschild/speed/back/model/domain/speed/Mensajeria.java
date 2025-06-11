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
@Table(name = "MENSAJERIA")
public class Mensajeria implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_mensajeria",nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_direccion",nullable=false)
	private Direccion direccion;

	@ManyToOne
	@JoinColumn(name="id_tipo_envio",nullable=false)
	private Parametro tipoEnvio;

	@ManyToOne
	@JoinColumn(name="id_ambito_envio")
	private Parametro ambitoEnvio;
	
	@ManyToOne
	@JoinColumn(name="id_empresa_destino")
	private Parametro empresaDestino;
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;
	
	@Column(nullable=false)
	private Character estado;
	
	@Column(name="estado_proceso",nullable=false)
	private Character estadoProceso;
	
	@ManyToOne
	@JoinColumn(name="remitente")
	private Usuario remitente;
	
	@ManyToMany
	@JoinTable(name="DOCUMENTO_MENSAJERIA",joinColumns={@JoinColumn(name="id_mensajeria")},inverseJoinColumns={@JoinColumn(name="id_documento")})
	private List<Documento> documentos;

	public Mensajeria() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}